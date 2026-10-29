package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LodhranAreas
import com.example.ui.components.DemoModeBadge
import com.example.ui.theme.PureError
import com.example.ui.theme.PureErrorContainer
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    uiState: PureFoodUiState,
    viewModel: PureFoodViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val formatter = NumberFormat.getNumberInstance(Locale.US)
    var areaDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("checkout_screen")
    ) {
        DemoModeBadge()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "آرڈر کی تفصیلات و ڈیلیوری فارم",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = PureGreenPrimary
        )
        Text(
            text = "براہ کرم لودھراں میں اپنے گھر کا پتہ اور رابطہ درج کریں",
            style = MaterialTheme.typography.bodyMedium,
            color = PureTextSecondary
        )

        // Error Banner if validation fails
        if (uiState.checkoutError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth().testTag("checkout_error_banner"),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = PureErrorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = PureError,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = uiState.checkoutError,
                        style = MaterialTheme.typography.bodySmall,
                        color = PureError,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Customer Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PureSurface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "گاہک کی معلومات",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PureTextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Name field
                OutlinedTextField(
                    value = uiState.customerName,
                    onValueChange = { viewModel.updateCheckoutName(it) },
                    modifier = Modifier.fillMaxWidth().testTag("checkout_name_input"),
                    label = { Text("آپ کا پورا نام") },
                    placeholder = { Text("مثلاً: محمد اکرم سکھیرہ") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = PureGreenPrimary)
                    },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Pakistani Mobile Number field
                OutlinedTextField(
                    value = uiState.customerPhone,
                    onValueChange = { viewModel.updateCheckoutPhone(it) },
                    modifier = Modifier.fillMaxWidth().testTag("checkout_phone_input"),
                    label = { Text("پاکستانی موبائل نمبر") },
                    placeholder = { Text("03001234567") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = PureGreenPrimary)
                    },
                    supportingText = {
                        Text("11 ہندسوں کا درست نمبر (03 سے شروع ہو)", fontSize = 11.sp)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Delivery Address in Lodhran Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PureSurface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "لودھراں میں ترسیل کا پتہ",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PureTextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Lodhran Area Selector
                ExposedDropdownMenuBox(
                    expanded = areaDropdownExpanded,
                    onExpandedChange = { areaDropdownExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.lodhranArea,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("لودھراں کا علاقہ / محلہ") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = PureGreenPrimary)
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = areaDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("checkout_area_dropdown"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = areaDropdownExpanded,
                        onDismissRequest = { areaDropdownExpanded = false }
                    ) {
                        LodhranAreas.ALL_AREAS.forEach { area ->
                            DropdownMenuItem(
                                text = { Text(area, style = MaterialTheme.typography.bodyMedium) },
                                onClick = {
                                    viewModel.updateCheckoutArea(area)
                                    areaDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Detailed address
                OutlinedTextField(
                    value = uiState.addressDetail,
                    onValueChange = { viewModel.updateCheckoutAddress(it) },
                    modifier = Modifier.fillMaxWidth().testTag("checkout_address_input"),
                    label = { Text("گھر کا پتہ اور قریبی نشانی") },
                    placeholder = { Text("گلی نمبر 4، نزد جامع مسجد، مکان نمبر 12") },
                    leadingIcon = {
                        Icon(Icons.Default.Home, contentDescription = null, tint = PureGreenPrimary)
                    },
                    shape = RoundedCornerShape(10.dp),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Optional delivery notes
                OutlinedTextField(
                    value = uiState.deliveryNotes,
                    onValueChange = { viewModel.updateCheckoutNotes(it) },
                    modifier = Modifier.fillMaxWidth().testTag("checkout_notes_input"),
                    label = { Text("خصوصی ہدایات (اختیاری)") },
                    placeholder = { Text("مثلاً: شام 5 بجے کے بعد لائیں یا گیٹ پر گھنٹی بجائیں") },
                    leadingIcon = {
                        Icon(Icons.Default.Note, contentDescription = null, tint = PureTextSecondary)
                    },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Payment Method Card (Cash on Delivery)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PureSurface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "طریقہ ادائیگی",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PureTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PureGreenContainer.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, PureGreenPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = true,
                            onClick = {},
                            colors = RadioButtonDefaults.colors(selectedColor = PureGreenPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            tint = PureGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "کیش آن ڈیلیوری (نقد رقم بوقت وصولی)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PureTextPrimary
                            )
                            Text(
                                text = "سودا گھر پر موصول اور تسلی کرنے کے بعد رائیڈر کو نقد ادائیگی کریں",
                                style = MaterialTheme.typography.bodySmall,
                                color = PureTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bill Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PureSurface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "بل کا خلاصہ",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PureTextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("اشیاء کی کل رقم:", style = MaterialTheme.typography.bodyMedium, color = PureTextSecondary)
                    Text("${formatter.format(uiState.cartSubtotal.toLong())} روپے", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("ہوم ڈیلیوری فیس (لودھراں):", style = MaterialTheme.typography.bodyMedium, color = PureTextSecondary)
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("کل قابل ادائیگی رقم:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${formatter.format(uiState.cartGrandTotal.toLong())} روپے",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PureGreenPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Submit Order Button
        Button(
            onClick = { viewModel.submitOrder() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("submit_order_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PureGreenPrimary),
            enabled = !uiState.isSubmittingOrder
        ) {
            if (uiState.isSubmittingOrder) {
                CircularProgressIndicator(
                    color = PureSurface,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "آرڈر جمع کروائیں (${formatter.format(uiState.cartGrandTotal.toLong())} روپے)",
                    style = MaterialTheme.typography.labelLarge,
                    color = PureSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
