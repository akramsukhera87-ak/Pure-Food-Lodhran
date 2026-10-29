package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeliveryConfig
import com.example.ui.components.DemoModeBadge
import com.example.ui.navigation.Screen
import com.example.ui.theme.PureError
import com.example.ui.theme.PureGreenContainer
import com.example.ui.theme.PureGreenPrimary
import com.example.ui.theme.PureSurface
import com.example.ui.theme.PureTextPrimary
import com.example.ui.theme.PureTextSecondary
import com.example.ui.theme.PureWheatContainer
import com.example.ui.theme.PureWheatGold
import com.example.ui.viewmodel.PureFoodUiState
import com.example.ui.viewmodel.PureFoodViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(
    uiState: PureFoodUiState,
    viewModel: PureFoodViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    var showClearCartDialog by remember { mutableStateOf(false) }
    val formatter = NumberFormat.getNumberInstance(Locale.US)

    if (showClearCartDialog) {
        AlertDialog(
            onDismissRequest = { showClearCartDialog = false },
            title = {
                Text(
                    text = "ٹوکری خالی کریں؟",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "کیا آپ واقعی شاپنگ کارٹ سے تمام اشیاء ہٹانا چاہتے ہیں؟",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearCart()
                        showClearCartDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PureError)
                ) {
                    Text("ہاں، خالی کریں")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCartDialog = false }) {
                    Text("منسوخ کریں")
                }
            }
        )
    }

    if (uiState.cartItems.isEmpty()) {
        // Empty Cart State
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("cart_empty_state"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = PureGreenContainer,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "خالی کارٹ",
                        tint = PureGreenPrimary,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "آپ کی ٹوکری خالی ہے",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PureTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "آپ نے ابھی تک کوئی گروسری یا راشن منتخب نہیں کیا۔ آٹا، دیسی گھی، چاول، دالیں یا ماہانہ راشن پیکجز شامل کریں۔",
                style = MaterialTheme.typography.bodyMedium,
                color = PureTextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.navigateTo(Screen.Categories) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PureGreenPrimary),
                modifier = Modifier.testTag("start_shopping_button")
            ) {
                Text("گروسری کی خریداری شروع کریں", style = MaterialTheme.typography.labelLarge)
            }
        }
    } else {
        // Cart with items
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("cart_screen")
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    DemoModeBadge()
                }

                // Free Delivery Threshold Tracker
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (uiState.cartSubtotal >= DeliveryConfig.FREE_DELIVERY_THRESHOLD)
                                PureGreenContainer else PureWheatContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (uiState.cartSubtotal >= DeliveryConfig.FREE_DELIVERY_THRESHOLD)
                                            Icons.Default.CheckCircle else Icons.Default.LocalShipping,
                                        contentDescription = null,
                                        tint = if (uiState.cartSubtotal >= DeliveryConfig.FREE_DELIVERY_THRESHOLD)
                                            PureGreenPrimary else PureWheatGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (uiState.cartSubtotal >= DeliveryConfig.FREE_DELIVERY_THRESHOLD)
                                            "مفت ڈیلیوری اہل!"
                                        else
                                            "مفت ہوم ڈیلیوری کیلئے مزید خریداری",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = PureTextPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            val progress = (uiState.cartSubtotal / DeliveryConfig.FREE_DELIVERY_THRESHOLD).toFloat().coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = PureGreenPrimary,
                                trackColor = PureSurface
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (uiState.cartSubtotal >= DeliveryConfig.FREE_DELIVERY_THRESHOLD)
                                    "مبارک ہو! لودھراں شہر میں آپ کے اس آرڈر پر ڈیلیوری بالکل مفت ہے۔"
                                else
                                    "مزید ${formatter.format(uiState.freeDeliveryRemaining.toLong())} روپے کی اشیاء شامل کریں اور 150 روپے ڈیلیوری فیس بچائیں!",
                                style = MaterialTheme.typography.bodySmall,
                                color = PureTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Header with item count and clear cart button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "منتخب کردہ اشیاء (${uiState.cartTotalItemCount})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureTextPrimary
                        )

                        TextButton(
                            onClick = { showClearCartDialog = true },
                            modifier = Modifier.testTag("clear_cart_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = PureError,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "کارٹ خالی کریں",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureError
                            )
                        }
                    }
                }

                // Cart item list
                items(uiState.cartItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cart_item_${item.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = PureSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.nameUrdu,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PureTextPrimary,
                                    lineHeight = 22.sp
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Surface(
                                    color = PureGreenContainer,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = item.selectedUnit,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PureGreenPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 11.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "فی یونٹ: ${formatter.format(item.unitPrice.toLong())} روپے",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PureTextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            // Stepper and item total
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PureGreenContainer.copy(alpha = 0.7f))
                                        .padding(2.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.updateCartItemQuantity(item.id, item.quantity - 1) },
                                        modifier = Modifier.size(32.dp).testTag("cart_item_decrement_${item.id}")
                                    ) {
                                        Icon(
                                            imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                                            contentDescription = "کم کریں",
                                            tint = if (item.quantity == 1) PureError else PureGreenPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    Text(
                                        text = item.quantity.toString(),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = PureGreenPrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )

                                    IconButton(
                                        onClick = { viewModel.updateCartItemQuantity(item.id, item.quantity + 1) },
                                        modifier = Modifier.size(32.dp).testTag("cart_item_increment_${item.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "بڑھائیں",
                                            tint = PureGreenPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "${formatter.format(item.totalPrice.toLong())} روپے",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PureGreenPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Summary Card & Checkout Button at Bottom
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = PureSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Subtotal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "اشیاء کی رقم (سب ٹوٹل):",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PureTextSecondary
                        )
                        Text(
                            text = "${formatter.format(uiState.cartSubtotal.toLong())} روپے",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = PureTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Delivery fee
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ہوم ڈیلیوری لودھراں:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PureTextSecondary
                        )
                        Text(
                            text = if (uiState.deliveryFee == 0.0) "مفت (Free)" else "${formatter.format(uiState.deliveryFee.toLong())} روپے",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.deliveryFee == 0.0) PureGreenPrimary else PureTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Grand total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ادائیگی کی کل رقم:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureTextPrimary
                        )
                        Text(
                            text = "${formatter.format(uiState.cartGrandTotal.toLong())} روپے",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = PureGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.navigateTo(Screen.Checkout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("proceed_to_checkout_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PureGreenPrimary)
                    ) {
                        Text(
                            text = "آرڈر مکمل کرنے کیلئے آگے بڑھیں",
                            style = MaterialTheme.typography.labelLarge,
                            color = PureSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
