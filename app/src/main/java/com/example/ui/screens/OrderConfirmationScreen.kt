package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeliveryConfig
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
fun OrderConfirmationScreen(
    orderId: String,
    uiState: PureFoodUiState,
    viewModel: PureFoodViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formatter = NumberFormat.getNumberInstance(Locale.US)

    // Intercept back to always go home cleanly
    BackHandler {
        viewModel.navigateToHome()
    }

    val placedOrder = uiState.orders.firstOrNull { it.orderId == orderId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("order_confirmation_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DemoModeBadge()

        Spacer(modifier = Modifier.height(16.dp))

        // Large Success Check
        Surface(
            shape = CircleShape,
            color = PureGreenContainer,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "کامیابی",
                    tint = PureGreenPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "آپ کا آرڈر موصول ہو گیا ہے!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = PureGreenPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "آرڈر نمبر: $orderId",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PureWheatGold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "پیور فوڈ لودھراں کا انتخاب کرنے کا شکریہ۔ آپ کا خالص سودا تیار کیا جا رہا ہے۔",
            style = MaterialTheme.typography.bodyMedium,
            color = PureTextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Demo Storage Guarantee Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = PureWheatContainer)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = PureWheatGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "آرڈر مقامی ڈیٹا بیس میں محفوظ ہے",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "یہ آرڈر اس ایپ کے آف لائن اسٹوریج میں بحفاظت محفوظ ہو چکا ہے اور آپ 'میرے آرڈرز' میں کسی بھی وقت دیکھ سکتے ہیں۔",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Order Summary Card
        if (placedOrder != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PureSurface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "آرڈر کی مختصر تفصیلات",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PureTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("گاہک کا نام:", style = MaterialTheme.typography.bodySmall, color = PureTextSecondary)
                        Text(placedOrder.customerName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("فون نمبر:", style = MaterialTheme.typography.bodySmall, color = PureTextSecondary)
                        Text(placedOrder.customerPhone, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("علاقہ (لودھراں):", style = MaterialTheme.typography.bodySmall, color = PureTextSecondary)
                        Text(placedOrder.lodhranArea, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("تفصیلی پتہ:", style = MaterialTheme.typography.bodySmall, color = PureTextSecondary)
                        Text(placedOrder.addressDetail, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("طریقہ کار:", style = MaterialTheme.typography.bodySmall, color = PureTextSecondary)
                        Text(placedOrder.paymentMethod, style = MaterialTheme.typography.bodySmall, color = PureGreenPrimary, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("کل رقم (نقد بوقت وصولی):", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(
                            "${formatter.format(placedOrder.grandTotal.toLong())} روپے",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = PureGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "متوقع ترسیل: آج شام لودھراں میں (3 سے 5 گھنٹے)",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureGreenPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Button(
            onClick = { viewModel.navigateTo(Screen.MyOrders) },
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("view_my_orders_btn"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PureGreenPrimary)
        ) {
            Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("میرے تمام آرڈرز دیکھیں", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${DeliveryConfig.STORE_PHONE}")
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("call_store_btn"),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Phone, contentDescription = null, tint = PureGreenPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("ہیلپ لائن پر کال کریں (${DeliveryConfig.STORE_PHONE})", style = MaterialTheme.typography.labelLarge, color = PureGreenPrimary)
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = { viewModel.navigateToHome() },
            modifier = Modifier.testTag("continue_shopping_btn")
        ) {
            Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("مزید خریداری کریں (ہوم اسکرین)", style = MaterialTheme.typography.labelMedium)
        }
    }
}
