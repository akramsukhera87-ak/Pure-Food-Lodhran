package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyOrdersScreen(
    uiState: PureFoodUiState,
    viewModel: PureFoodViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val formatter = NumberFormat.getNumberInstance(Locale.US)
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US)

    if (uiState.orders.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("my_orders_empty_state"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = PureGreenContainer,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = "کوئی آرڈر نہیں",
                        tint = PureGreenPrimary,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "ابھی تک کوئی آرڈر نہیں دیا گیا",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PureTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "جب آپ لودھراں کیلئے راشن یا گروسری کا آرڈر کریں گے تو وہ یہاں محفوظ اور ظاہر ہو گا۔",
                style = MaterialTheme.typography.bodyMedium,
                color = PureTextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.navigateToHome() },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PureGreenPrimary),
                modifier = Modifier.testTag("start_shopping_from_orders")
            ) {
                Text("گروسری کی خریداری شروع کریں", style = MaterialTheme.typography.labelLarge)
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("my_orders_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DemoModeBadge()
            }

            item {
                Column {
                    Text(
                        text = "میرے سابقہ آرڈرز",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PureGreenPrimary
                    )
                    Text(
                        text = "کل ${uiState.orders.size} آرڈرز مقامی ڈیٹا بیس میں محفوظ ہیں",
                        style = MaterialTheme.typography.bodySmall,
                        color = PureTextSecondary
                    )
                }
            }

            items(uiState.orders) { order ->
                OrderItemCard(order = order, formatter = formatter, dateFormat = dateFormat)
            }
        }
    }
}

@Composable
private fun OrderItemCard(
    order: Order,
    formatter: NumberFormat,
    dateFormat: SimpleDateFormat
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("order_card_${order.orderId}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID and Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "آرڈر #${order.orderId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PureGreenPrimary
                )

                Surface(
                    color = PureGreenContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = order.statusUrdu,
                        style = MaterialTheme.typography.labelSmall,
                        color = PureGreenPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Date & Lodhran locality
            Text(
                text = dateFormat.format(Date(order.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = PureTextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "پتہ: ${order.lodhranArea}، ${order.addressDetail}",
                style = MaterialTheme.typography.bodySmall,
                color = PureTextPrimary,
                fontSize = 12.sp,
                maxLines = if (expanded) Int.MAX_VALUE else 1
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(10.dp))

            // Items breakdown if expanded
            if (expanded && order.items.isNotEmpty()) {
                Text(
                    text = "آرڈر کی اشیاء:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = PureTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                order.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.nameUrdu} (${item.selectedUnit}) × ${item.quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureTextSecondary
                        )
                        Text(
                            text = "${formatter.format(item.totalPrice.toLong())} روپے",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = PureTextPrimary
                        )
                    }
                }

                if (order.deliveryNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ہدایات: ${order.deliveryNotes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = PureWheatGold,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Total and expand chevron
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "کل رقم: ",
                        style = MaterialTheme.typography.titleSmall,
                        color = PureTextSecondary
                    )
                    Text(
                        text = "${formatter.format(order.grandTotal.toLong())} روپے",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PureGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = PureWheatContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "نقد بوقت وصولی",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "کم کریں" else "تفصیل دیکھیں",
                    tint = PureTextSecondary
                )
            }
        }
    }
}
