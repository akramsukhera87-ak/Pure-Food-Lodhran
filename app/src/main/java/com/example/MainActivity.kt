package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.PureFoodDatabase
import com.example.data.repository.LocalPureFoodRepository
import com.example.ui.components.PureFoodBottomBar
import com.example.ui.components.PureFoodTopBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyOrdersScreen
import com.example.ui.screens.OrderConfirmationScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProductListScreen
import com.example.ui.theme.PureFoodTheme
import com.example.ui.viewmodel.PureFoodViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = PureFoodDatabase.getDatabase(applicationContext)
        val repository = LocalPureFoodRepository(database.cartDao(), database.orderDao())
        val viewModelFactory = PureFoodViewModel.Factory(repository)

        setContent {
            val viewModel: PureFoodViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = viewModelFactory
            )

            PureFoodApp(viewModel = viewModel)
        }
    }
}

@Composable
fun PureFoodApp(viewModel: PureFoodViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show feedback messages as Snackbars
    LaunchedEffect(uiState.feedbackMessage) {
        uiState.feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissFeedbackMessage()
        }
    }

    PureFoodTheme {
        // Enforce RTL Layout Direction for Urdu Language UX
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            val titleAndSubtitle = when (val screen = uiState.currentScreen) {
                is Screen.Home -> Pair("پیور فوڈ لودھراں", "خالص راشن و گھریلو سودا سلف")
                is Screen.Categories -> Pair("گروسری کیٹگریز", "تمام شعبہ جات")
                is Screen.ProductList -> {
                    val catName = uiState.categories.firstOrNull { it.id == uiState.selectedCategoryId }?.nameUrdu
                    Pair(catName ?: "اشیاء کی فہرست", "خالص اور معیاری راشن")
                }
                is Screen.ProductDetail -> Pair(
                    uiState.selectedProduct?.nameUrdu ?: "تفصیلات",
                    "پروڈکٹ کی مکمل معلومات"
                )
                is Screen.Cart -> Pair("شاپنگ کارٹ (ٹوکری)", "${uiState.cartTotalItemCount} اشیاء منتخب شدہ")
                is Screen.Checkout -> Pair("آرڈر مکمل کریں", "لودھراں میں ہوم ڈیلیوری")
                is Screen.OrderConfirmation -> Pair("آرڈر کی تصدیق", "شکریہ! آرڈر محفوظ ہو گیا")
                is Screen.MyOrders -> Pair("میرے آرڈرز", "${uiState.orders.size} آرڈرز محفوظ شدہ")
                is Screen.Contact -> Pair("رابطہ و کسٹمر سپورٹ", "پیور فوڈ لودھراں ہیلپ لائن")
                is Screen.About -> Pair("ہمارے متعلق", "پیور فوڈ کا مشن اور تعارف")
            }

            val canGoBack = uiState.currentScreen !is Screen.Home

            // Only show bottom navigation on main tabs
            val showBottomBar = uiState.currentScreen is Screen.Home ||
                    uiState.currentScreen is Screen.Categories ||
                    uiState.currentScreen is Screen.Cart ||
                    uiState.currentScreen is Screen.MyOrders ||
                    uiState.currentScreen is Screen.Contact

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    PureFoodTopBar(
                        titleUrdu = titleAndSubtitle.first,
                        subtitleUrdu = titleAndSubtitle.second,
                        canNavigateBack = canGoBack,
                        onNavigateBack = { viewModel.navigateBack() },
                        cartItemCount = uiState.cartTotalItemCount,
                        onCartClick = { viewModel.navigateTo(Screen.Cart) },
                        onAboutClick = { viewModel.navigateTo(Screen.About) }
                    )
                },
                bottomBar = {
                    if (showBottomBar) {
                        PureFoodBottomBar(
                            currentScreen = uiState.currentScreen,
                            cartItemCount = uiState.cartTotalItemCount,
                            onNavigate = { screen -> viewModel.navigateTo(screen) }
                        )
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { innerPadding ->
                val contentModifier = Modifier.padding(innerPadding)

                when (val screen = uiState.currentScreen) {
                    is Screen.Home -> HomeScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.Categories -> CategoriesScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.ProductList -> ProductListScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.ProductDetail -> ProductDetailScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.Cart -> CartScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.Checkout -> CheckoutScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.OrderConfirmation -> OrderConfirmationScreen(
                        orderId = screen.orderId,
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.MyOrders -> MyOrdersScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.Contact -> ContactScreen(
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                    is Screen.About -> AboutScreen(
                        viewModel = viewModel,
                        modifier = contentModifier
                    )
                }
            }
        }
    }
}
