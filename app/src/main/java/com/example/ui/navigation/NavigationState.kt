package com.example.ui.navigation

sealed class Screen {
    object Home : Screen()
    object Categories : Screen()
    data class ProductList(val categoryId: String? = null, val searchQuery: String? = null) : Screen()
    data class ProductDetail(val productId: String) : Screen()
    object Cart : Screen()
    object Checkout : Screen()
    data class OrderConfirmation(val orderId: String) : Screen()
    object MyOrders : Screen()
    object Contact : Screen()
    object About : Screen()
}

enum class BottomNavItem(
    val titleUrdu: String,
    val screen: Screen
) {
    HOME("ہوم", Screen.Home),
    CATEGORIES("کیٹگریز", Screen.Categories),
    CART("ٹوکری", Screen.Cart),
    MY_ORDERS("میرے آرڈرز", Screen.MyOrders),
    CONTACT("رابطہ", Screen.Contact)
}
