package com.example.data.repository

import com.example.data.local.CartDao
import com.example.data.local.CartItemEntity
import com.example.data.local.OrderDao
import com.example.data.local.OrderEntity
import com.example.data.local.OrderItemEntity
import com.example.data.model.CartItem
import com.example.data.model.Category
import com.example.data.model.GroceryCategories
import com.example.data.model.Order
import com.example.data.model.OrderItem
import com.example.data.model.Product
import com.example.data.model.SampleProducts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository interface preparing for future Firebase Cloud Firestore sync
 * while providing immediate local offline durability via Room Database.
 */
interface PureFoodRepository {
    fun getCategories(): List<Category>
    fun getProducts(): List<Product>
    fun getProductById(id: String): Product?
    fun getProductsByCategory(categoryId: String): List<Product>
    fun searchProducts(query: String): List<Product>

    // Cart operations
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(product: Product, unitLabel: String, quantity: Int = 1)
    suspend fun updateCartQuantity(cartItemId: String, newQuantity: Int)
    suspend fun removeCartItem(cartItemId: String)
    suspend fun clearCart()

    // Order operations
    fun getOrders(): Flow<List<Order>>
    suspend fun placeOrder(order: Order): Result<String>
    suspend fun getOrderById(orderId: String): Order?
}

class LocalPureFoodRepository(
    private val cartDao: CartDao,
    private val orderDao: OrderDao
) : PureFoodRepository {

    override fun getCategories(): List<Category> = GroceryCategories.ALL_CATEGORIES

    override fun getProducts(): List<Product> = SampleProducts.PRODUCTS

    override fun getProductById(id: String): Product? {
        return SampleProducts.PRODUCTS.firstOrNull { it.id == id }
    }

    override fun getProductsByCategory(categoryId: String): List<Product> {
        return SampleProducts.PRODUCTS.filter { it.categoryId == categoryId }
    }

    override fun searchProducts(query: String): List<Product> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return SampleProducts.PRODUCTS
        return SampleProducts.PRODUCTS.filter {
            it.nameUrdu.contains(trimmed, ignoreCase = true) ||
                    it.nameEnglish.contains(trimmed, ignoreCase = true) ||
                    it.descriptionUrdu.contains(trimmed, ignoreCase = true)
        }
    }

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems().map { entities ->
            entities.map { entity ->
                CartItem(
                    id = entity.id,
                    productId = entity.productId,
                    nameUrdu = entity.nameUrdu,
                    nameEnglish = entity.nameEnglish,
                    categoryId = entity.categoryId,
                    selectedUnit = entity.selectedUnit,
                    unitPrice = entity.unitPrice,
                    quantity = entity.quantity
                )
            }
        }
    }

    override suspend fun addToCart(product: Product, unitLabel: String, quantity: Int) {
        val cartItemId = "${product.id}_${unitLabel}"
        val unitPrice = product.getPriceForUnit(unitLabel)
        val entity = CartItemEntity(
            id = cartItemId,
            productId = product.id,
            nameUrdu = product.nameUrdu,
            nameEnglish = product.nameEnglish,
            categoryId = product.categoryId,
            selectedUnit = unitLabel,
            unitPrice = unitPrice,
            quantity = quantity
        )
        cartDao.insertOrUpdate(entity)
    }

    override suspend fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            cartDao.deleteItemById(cartItemId)
        } else {
            cartDao.updateQuantity(cartItemId, newQuantity)
        }
    }

    override suspend fun removeCartItem(cartItemId: String) {
        cartDao.deleteItemById(cartItemId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override fun getOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { orderEntities ->
            orderEntities.map { entity ->
                val items = orderDao.getItemsForOrder(entity.orderId).map { itemEntity ->
                    OrderItem(
                        productId = itemEntity.productId,
                        nameUrdu = itemEntity.nameUrdu,
                        selectedUnit = itemEntity.selectedUnit,
                        unitPrice = itemEntity.unitPrice,
                        quantity = itemEntity.quantity,
                        totalPrice = itemEntity.totalPrice
                    )
                }
                Order(
                    orderId = entity.orderId,
                    customerName = entity.customerName,
                    customerPhone = entity.customerPhone,
                    lodhranArea = entity.lodhranArea,
                    addressDetail = entity.addressDetail,
                    deliveryNotes = entity.deliveryNotes,
                    subtotal = entity.subtotal,
                    deliveryFee = entity.deliveryFee,
                    grandTotal = entity.grandTotal,
                    paymentMethod = entity.paymentMethod,
                    timestamp = entity.timestamp,
                    statusUrdu = entity.statusUrdu,
                    isDemo = entity.isDemo,
                    items = items
                )
            }
        }
    }

    override suspend fun placeOrder(order: Order): Result<String> {
        return try {
            val orderEntity = OrderEntity(
                orderId = order.orderId,
                customerName = order.customerName,
                customerPhone = order.customerPhone,
                lodhranArea = order.lodhranArea,
                addressDetail = order.addressDetail,
                deliveryNotes = order.deliveryNotes,
                subtotal = order.subtotal,
                deliveryFee = order.deliveryFee,
                grandTotal = order.grandTotal,
                paymentMethod = order.paymentMethod,
                timestamp = order.timestamp,
                statusUrdu = order.statusUrdu,
                isDemo = order.isDemo
            )
            val itemEntities = order.items.map { item ->
                OrderItemEntity(
                    orderId = order.orderId,
                    productId = item.productId,
                    nameUrdu = item.nameUrdu,
                    selectedUnit = item.selectedUnit,
                    unitPrice = item.unitPrice,
                    quantity = item.quantity,
                    totalPrice = item.totalPrice
                )
            }
            orderDao.insertOrder(orderEntity)
            orderDao.insertOrderItems(itemEntities)
            // Empty cart after successful placement
            cartDao.clearCart()
            Result.success(order.orderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrderById(orderId: String): Order? {
        val entity = orderDao.getOrderById(orderId) ?: return null
        val items = orderDao.getItemsForOrder(orderId).map { itemEntity ->
            OrderItem(
                productId = itemEntity.productId,
                nameUrdu = itemEntity.nameUrdu,
                selectedUnit = itemEntity.selectedUnit,
                unitPrice = itemEntity.unitPrice,
                quantity = itemEntity.quantity,
                totalPrice = itemEntity.totalPrice
            )
        }
        return Order(
            orderId = entity.orderId,
            customerName = entity.customerName,
            customerPhone = entity.customerPhone,
            lodhranArea = entity.lodhranArea,
            addressDetail = entity.addressDetail,
            deliveryNotes = entity.deliveryNotes,
            subtotal = entity.subtotal,
            deliveryFee = entity.deliveryFee,
            grandTotal = entity.grandTotal,
            paymentMethod = entity.paymentMethod,
            timestamp = entity.timestamp,
            statusUrdu = entity.statusUrdu,
            isDemo = entity.isDemo,
            items = items
        )
    }
}
