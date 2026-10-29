package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CartItem
import com.example.data.model.Category
import com.example.data.model.DeliveryConfig
import com.example.data.model.LodhranAreas
import com.example.data.model.Order
import com.example.data.model.OrderItem
import com.example.data.model.Product
import com.example.data.repository.PureFoodRepository
import com.example.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class ProductSortOption(val titleUrdu: String) {
    DEFAULT("معمول کی ترتیب"),
    PRICE_LOW_HIGH("قیمت: کم سے زیادہ"),
    PRICE_HIGH_LOW("قیمت: زیادہ سے کم"),
    NAME("نام کے لحاظ سے")
}

data class PureFoodUiState(
    val currentScreen: Screen = Screen.Home,
    val backStack: List<Screen> = listOf(Screen.Home),
    val categories: List<Category> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val orders: List<Order> = emptyList(),
    val selectedProduct: Product? = null,
    val selectedUnit: String = "",
    val detailQuantity: Int = 1,
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val sortOption: ProductSortOption = ProductSortOption.DEFAULT,

    // Checkout form state
    val customerName: String = "",
    val customerPhone: String = "",
    val lodhranArea: String = LodhranAreas.ALL_AREAS[0],
    val addressDetail: String = "",
    val deliveryNotes: String = "",
    val isSubmittingOrder: Boolean = false,
    val checkoutError: String? = null,
    val lastPlacedOrderId: String? = null,

    // Feedback
    val feedbackMessage: String? = null
) {
    val cartSubtotal: Double
        get() = cartItems.sumOf { it.totalPrice }

    val deliveryFee: Double
        get() = if (cartItems.isEmpty() || cartSubtotal >= DeliveryConfig.FREE_DELIVERY_THRESHOLD) 0.0 else DeliveryConfig.STANDARD_DELIVERY_FEE

    val freeDeliveryRemaining: Double
        get() = (DeliveryConfig.FREE_DELIVERY_THRESHOLD - cartSubtotal).coerceAtLeast(0.0)

    val cartGrandTotal: Double
        get() = cartSubtotal + deliveryFee

    val cartTotalItemCount: Int
        get() = cartItems.sumOf { it.quantity }

    val filteredProducts: List<Product>
        get() {
            var list = allProducts
            if (!selectedCategoryId.isNullOrBlank()) {
                list = list.filter { it.categoryId == selectedCategoryId }
            }
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim()
                list = list.filter {
                    it.nameUrdu.contains(q, ignoreCase = true) ||
                            it.nameEnglish.contains(q, ignoreCase = true) ||
                            it.descriptionUrdu.contains(q, ignoreCase = true)
                }
            }
            return when (sortOption) {
                ProductSortOption.DEFAULT -> list
                ProductSortOption.PRICE_LOW_HIGH -> list.sortedBy { it.basePrice }
                ProductSortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.basePrice }
                ProductSortOption.NAME -> list.sortedBy { it.nameUrdu }
            }
        }
}

class PureFoodViewModel(
    private val repository: PureFoodRepository
) : ViewModel() {

    private val _internalState = MutableStateFlow(
        PureFoodUiState(
            categories = repository.getCategories(),
            allProducts = repository.getProducts()
        )
    )

    val uiState: StateFlow<PureFoodUiState> = combine(
        _internalState,
        repository.getCartItems(),
        repository.getOrders()
    ) { state, cartItems, orders ->
        state.copy(
            cartItems = cartItems,
            orders = orders
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _internalState.value
    )

    // Navigation
    fun navigateTo(screen: Screen) {
        val currentStack = _internalState.value.backStack.toMutableList()
        currentStack.add(screen)
        _internalState.value = _internalState.value.copy(
            currentScreen = screen,
            backStack = currentStack
        )
    }

    fun navigateBack(): Boolean {
        val currentStack = _internalState.value.backStack.toMutableList()
        return if (currentStack.size > 1) {
            currentStack.removeAt(currentStack.size - 1)
            val previousScreen = currentStack.last()
            _internalState.value = _internalState.value.copy(
                currentScreen = previousScreen,
                backStack = currentStack
            )
            true
        } else {
            // Already at root, ensure on Home
            if (_internalState.value.currentScreen != Screen.Home) {
                _internalState.value = _internalState.value.copy(
                    currentScreen = Screen.Home,
                    backStack = listOf(Screen.Home)
                )
                true
            } else {
                false
            }
        }
    }

    fun navigateToHome() {
        _internalState.value = _internalState.value.copy(
            currentScreen = Screen.Home,
            backStack = listOf(Screen.Home)
        )
    }

    // Search and Filters
    fun onSearchQueryChanged(query: String) {
        _internalState.value = _internalState.value.copy(searchQuery = query)
    }

    fun selectCategory(categoryId: String?) {
        _internalState.value = _internalState.value.copy(selectedCategoryId = categoryId)
    }

    fun setSortOption(option: ProductSortOption) {
        _internalState.value = _internalState.value.copy(sortOption = option)
    }

    // Product Detail
    fun selectProduct(productId: String) {
        val product = repository.getProductById(productId)
        if (product != null) {
            _internalState.value = _internalState.value.copy(
                selectedProduct = product,
                selectedUnit = product.defaultUnit,
                detailQuantity = 1
            )
            navigateTo(Screen.ProductDetail(productId))
        }
    }

    fun changeSelectedUnit(unitLabel: String) {
        _internalState.value = _internalState.value.copy(selectedUnit = unitLabel)
    }

    fun incrementDetailQuantity() {
        val current = _internalState.value.detailQuantity
        if (current < 99) {
            _internalState.value = _internalState.value.copy(detailQuantity = current + 1)
        }
    }

    fun decrementDetailQuantity() {
        val current = _internalState.value.detailQuantity
        if (current > 1) {
            _internalState.value = _internalState.value.copy(detailQuantity = current - 1)
        }
    }

    fun addToCartFromDetail() {
        val product = _internalState.value.selectedProduct ?: return
        val unit = _internalState.value.selectedUnit
        val qty = _internalState.value.detailQuantity

        viewModelScope.launch {
            repository.addToCart(product, unit, qty)
            _internalState.value = _internalState.value.copy(
                feedbackMessage = "ٹوکری میں شامل کر دیا گیا (${product.nameUrdu})"
            )
        }
    }

    fun quickAddToCart(product: Product) {
        viewModelScope.launch {
            repository.addToCart(product, product.defaultUnit, 1)
            _internalState.value = _internalState.value.copy(
                feedbackMessage = "ٹوکری میں شامل کر دیا گیا (${product.nameUrdu})"
            )
        }
    }

    fun updateCartItemQuantity(cartItemId: String, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, newQuantity)
        }
    }

    fun removeCartItem(cartItemId: String) {
        viewModelScope.launch {
            repository.removeCartItem(cartItemId)
            _internalState.value = _internalState.value.copy(
                feedbackMessage = "آئٹم ٹوکری سے ہٹا دیا گیا"
            )
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            _internalState.value = _internalState.value.copy(
                feedbackMessage = "ٹوکری خالی کر دی گئی"
            )
        }
    }

    // Checkout Form
    fun updateCheckoutName(name: String) {
        _internalState.value = _internalState.value.copy(customerName = name, checkoutError = null)
    }

    fun updateCheckoutPhone(phone: String) {
        _internalState.value = _internalState.value.copy(customerPhone = phone, checkoutError = null)
    }

    fun updateCheckoutArea(area: String) {
        _internalState.value = _internalState.value.copy(lodhranArea = area)
    }

    fun updateCheckoutAddress(address: String) {
        _internalState.value = _internalState.value.copy(addressDetail = address, checkoutError = null)
    }

    fun updateCheckoutNotes(notes: String) {
        _internalState.value = _internalState.value.copy(deliveryNotes = notes)
    }

    fun submitOrder() {
        val state = uiState.value
        val items = state.cartItems

        if (items.isEmpty()) {
            _internalState.value = _internalState.value.copy(
                checkoutError = "آپ کی ٹوکری خالی ہے۔ آرڈر کیلئے اشیاء منتخب کریں!"
            )
            return
        }

        val name = state.customerName.trim()
        if (name.length < 3) {
            _internalState.value = _internalState.value.copy(
                checkoutError = "براہ کرم اپنا پورا نام درج کریں (کم از کم 3 حروف)"
            )
            return
        }

        val rawPhone = state.customerPhone.replace("-", "").replace(" ", "").trim()
        if (!rawPhone.matches(Regex("^03\\d{9}$"))) {
            _internalState.value = _internalState.value.copy(
                checkoutError = "براہ کرم درست پاکستانی موبائل نمبر درج کریں (مثلاً: 03001234567)"
            )
            return
        }

        val address = state.addressDetail.trim()
        if (address.length < 6) {
            _internalState.value = _internalState.value.copy(
                checkoutError = "براہ کرم لودھراں کا تفصیلی گلی، محلہ یا مکان نمبر درج کریں"
            )
            return
        }

        _internalState.value = _internalState.value.copy(isSubmittingOrder = true, checkoutError = null)

        viewModelScope.launch {
            // Generate clean human-readable order ID for Pure Food Lodhran
            val randomSuffix = (1000..9999).random()
            val orderId = "PF-LOD-$randomSuffix"

            val orderItems = items.map { cartItem ->
                OrderItem(
                    productId = cartItem.productId,
                    nameUrdu = cartItem.nameUrdu,
                    selectedUnit = cartItem.selectedUnit,
                    unitPrice = cartItem.unitPrice,
                    quantity = cartItem.quantity,
                    totalPrice = cartItem.totalPrice
                )
            }

            val order = Order(
                orderId = orderId,
                customerName = name,
                customerPhone = rawPhone,
                lodhranArea = state.lodhranArea,
                addressDetail = address,
                deliveryNotes = state.deliveryNotes.trim(),
                subtotal = state.cartSubtotal,
                deliveryFee = state.deliveryFee,
                grandTotal = state.cartGrandTotal,
                paymentMethod = "کیش آن ڈیلیوری (نقد رقم بوقت وصولی)",
                timestamp = System.currentTimeMillis(),
                statusUrdu = "موصول ہو گیا",
                isDemo = true,
                items = orderItems
            )

            val result = repository.placeOrder(order)
            _internalState.value = _internalState.value.copy(isSubmittingOrder = false)

            if (result.isSuccess) {
                _internalState.value = _internalState.value.copy(
                    lastPlacedOrderId = orderId,
                    customerName = "",
                    customerPhone = "",
                    addressDetail = "",
                    deliveryNotes = ""
                )
                navigateTo(Screen.OrderConfirmation(orderId))
            } else {
                _internalState.value = _internalState.value.copy(
                    checkoutError = "آرڈر محفوظ کرنے میں خرابی پیش آئی۔ دوبارہ کوشش کریں۔"
                )
            }
        }
    }

    fun dismissFeedbackMessage() {
        _internalState.value = _internalState.value.copy(feedbackMessage = null)
    }

    class Factory(private val repository: PureFoodRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PureFoodViewModel(repository) as T
        }
    }
}
