package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DemoModeBadge
import com.example.ui.navigation.Screen
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
fun ProductDetailScreen(
    uiState: PureFoodUiState,
    viewModel: PureFoodViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val product = uiState.selectedProduct
    if (product == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("کوئی پروڈکٹ منتخب نہیں کی گئی۔", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    val formatter = NumberFormat.getNumberInstance(Locale.US)
    val unitPrice = product.getPriceForUnit(uiState.selectedUnit)
    val totalPrice = unitPrice * uiState.detailQuantity

    val category = uiState.categories.firstOrNull { it.id == product.categoryId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("product_detail_screen")
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Demo badge
            DemoModeBadge()
            Spacer(modifier = Modifier.height(12.dp))

            // Main Product Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Category & Purity Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = PureGreenContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = category?.nameUrdu ?: "گروسری",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureGreenPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = PureGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "100% خالص لودھراں کوالٹی",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureGreenPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Urdu Title
                    Text(
                        text = product.nameUrdu,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PureTextPrimary,
                        lineHeight = 30.sp
                    )

                    // English Subtitle
                    Text(
                        text = product.nameEnglish,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PureTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Origin guarantee
                    Text(
                        text = "ماخذ: ${product.originOrGuarantee}",
                        style = MaterialTheme.typography.bodySmall,
                        color = PureWheatGold,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Unit Selection (e.g. 5kg, 10kg, 20kg or 1L, 5L)
                    Text(
                        text = "پیکنگ اور وزن منتخب کریں:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PureTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        product.unitOptions.forEach { option ->
                            FilterChip(
                                selected = uiState.selectedUnit == option.labelUrdu,
                                onClick = { viewModel.changeSelectedUnit(option.labelUrdu) },
                                label = {
                                    Text(
                                        text = option.labelUrdu,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PureGreenPrimary,
                                    selectedLabelColor = PureSurface
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Unit price display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "فی یونٹ قیمت (${uiState.selectedUnit}):",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PureTextSecondary
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = formatter.format(unitPrice.toLong()),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = PureGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "روپے",
                                style = MaterialTheme.typography.labelMedium,
                                color = PureGreenPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Urdu Description
                    Text(
                        text = "تفصیلات و خصوصیات:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PureTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.descriptionUrdu,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PureTextSecondary,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Lodhran Delivery Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWheatContainer)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = PureWheatGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "لودھراں شہر میں ہوم ڈیلیوری",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "2,000 روپے سے زائد آرڈر پر مفت ترسیل، نقد بوقت وصولی (Cash on Delivery)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Bottom Action Bar: Quantity Selector & Add to Cart Button
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            color = PureSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(PureGreenContainer)
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.decrementDetailQuantity() },
                            modifier = Modifier.size(36.dp).testTag("detail_decrement_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "کم کریں",
                                tint = PureGreenPrimary
                            )
                        }

                        Text(
                            text = uiState.detailQuantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureGreenPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )

                        IconButton(
                            onClick = { viewModel.incrementDetailQuantity() },
                            modifier = Modifier.size(36.dp).testTag("detail_increment_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "بڑھائیں",
                                tint = PureGreenPrimary
                            )
                        }
                    }

                    // Total for selected quantity
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "کل قیمت",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureTextSecondary
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = formatter.format(totalPrice.toLong()),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = PureGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "روپے",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureGreenPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.addToCartFromDetail() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("add_to_cart_detail_btn"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PureGreenPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ٹوکری میں شامل کریں (${formatter.format(totalPrice.toLong())} روپے)",
                        style = MaterialTheme.typography.labelLarge,
                        color = PureSurface
                    )
                }
            }
        }
    }
}
