package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.BottomNavItem
import com.example.ui.navigation.Screen
import com.example.ui.theme.PureGreenContainer
import com.example.ui.theme.PureGreenPrimary
import com.example.ui.theme.PureSurface

@Composable
fun PureFoodBottomBar(
    currentScreen: Screen,
    cartItemCount: Int,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("pure_food_bottom_bar"),
        containerColor = PureSurface,
        tonalElevation = androidx.compose.ui.unit.Dp(6f)
    ) {
        val items = listOf(
            Triple(BottomNavItem.HOME, Icons.Filled.Home, Icons.Outlined.Home),
            Triple(BottomNavItem.CATEGORIES, Icons.Filled.Category, Icons.Outlined.Category),
            Triple(BottomNavItem.CART, Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
            Triple(BottomNavItem.MY_ORDERS, Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong),
            Triple(BottomNavItem.CONTACT, Icons.Filled.Phone, Icons.Outlined.Phone)
        )

        items.forEach { (navItem, filledIcon, outlinedIcon) ->
            val isSelected = when (navItem) {
                BottomNavItem.HOME -> currentScreen is Screen.Home
                BottomNavItem.CATEGORIES -> currentScreen is Screen.Categories
                BottomNavItem.CART -> currentScreen is Screen.Cart
                BottomNavItem.MY_ORDERS -> currentScreen is Screen.MyOrders
                BottomNavItem.CONTACT -> currentScreen is Screen.Contact
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(navItem.screen) },
                icon = {
                    if (navItem == BottomNavItem.CART && cartItemCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ) {
                                    Text(
                                        text = cartItemCount.toString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) filledIcon else outlinedIcon,
                                contentDescription = navItem.titleUrdu
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) filledIcon else outlinedIcon,
                            contentDescription = navItem.titleUrdu
                        )
                    }
                },
                label = {
                    Text(
                        text = navItem.titleUrdu,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PureGreenPrimary,
                    selectedTextColor = PureGreenPrimary,
                    indicatorColor = PureGreenContainer
                ),
                modifier = Modifier.testTag("bottom_nav_${navItem.name.lowercase()}")
            )
        }
    }
}
