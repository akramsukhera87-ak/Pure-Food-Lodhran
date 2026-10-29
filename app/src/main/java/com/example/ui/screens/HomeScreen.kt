package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DeliveryConfig
import com.example.data.model.Product
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.ProductCard
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
fun HomeScreen(
    uiState: PureFoodUiState,
    viewModel: PureFoodViewModel,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getNumberInstance(Locale.US)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Demo Mode Banner
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                DemoModeBadge()
            }
        }

        // 2. Hero Banner Image with Welcome & Pure Grocery Mission
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(165.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = "پیور فوڈ لودھراں راشن و سودا سلف",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        androidx.compose.ui.graphics.Color.Transparent,
                                        androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Surface(
                            color = PureWheatGold,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "لودھراں کی اپنی گروسری سروس",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureSurface,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "خالص آٹا، دیسی گھی، باسمتی چاول اور راشن",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureSurface
                        )
                        Text(
                            text = "گھر کی دہلیز پر فوری ترسیل - بغیر ملاوٹ کی ضمانت",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureSurface.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // 3. Search Bar
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = {
                        viewModel.onSearchQueryChanged(it)
                        if (it.isNotBlank()) {
                            viewModel.navigateTo(Screen.ProductList(searchQuery = it))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input"),
                    placeholder = {
                        Text(
                            text = "آٹا، دیسی گھی، چاول، دالیں تلاش کریں...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PureTextSecondary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "تلاش",
                            tint = PureGreenPrimary
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureSurface,
                        unfocusedContainerColor = PureSurface
                    ),
                    singleLine = true
                )
            }
        }

        // 4. Free Delivery Threshold Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("free_delivery_card"),
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
                                    "مبارک ہو! مفت ہوم ڈیلیوری حاصل ہو گئی"
                                else
                                    "مفت ڈیلیوری آفر (لودھراں شہر)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PureTextPrimary
                            )
                        }

                        Text(
                            text = "حد: 2,000 روپے",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val progress = (uiState.cartSubtotal / DeliveryConfig.FREE_DELIVERY_THRESHOLD).toFloat().coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PureGreenPrimary,
                        trackColor = PureSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (uiState.cartSubtotal >= DeliveryConfig.FREE_DELIVERY_THRESHOLD)
                            "آپ کا آرڈر 2,000 روپے سے زائد ہے۔ لودھراں شہر میں کوئی ڈیلیوری چارجز نہیں!"
                        else
                            "مفت ڈیلیوری حاصل کرنے کیلئے مزید ${formatter.format(uiState.freeDeliveryRemaining.toLong())} روپے کی خریداری کریں۔ (عام فیس: 150 روپے)",
                        style = MaterialTheme.typography.bodySmall,
                        color = PureTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // 5. Quick Category Icons
        item {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "اہم شعبہ جات (کیٹگریز)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PureTextPrimary
                    )
                    TextButton(
                        onClick = { viewModel.navigateTo(Screen.Categories) }
                    ) {
                        Text(
                            text = "سب دیکھیں",
                            style = MaterialTheme.typography.labelMedium,
                            color = PureGreenPrimary
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.categories) { category ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(78.dp)
                                .clickable {
                                    viewModel.selectCategory(category.id)
                                    viewModel.navigateTo(Screen.ProductList(categoryId = category.id))
                                }
                                .testTag("home_category_${category.id}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(PureGreenContainer)
                                    .border(1.dp, PureGreenPrimary.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = category.nameUrdu,
                                    tint = PureGreenPrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = category.nameUrdu,
                                style = MaterialTheme.typography.labelSmall,
                                color = PureTextPrimary,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // 6. Featured Monthly Ration Packages (ماہانہ راشن پیکجز)
        val rationPackages = uiState.allProducts.filter { it.isRationPackage }
        if (rationPackages.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(top = 18.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ماہانہ راشن پیکجز",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PureTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = PureWheatGold,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "بچت پیک",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PureSurface,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                )
                            }
                        }

                        TextButton(
                            onClick = {
                                viewModel.selectCategory("cat_ration")
                                viewModel.navigateTo(Screen.ProductList(categoryId = "cat_ration"))
                            }
                        ) {
                            Text(
                                text = "مزید راشن پیکجز",
                                style = MaterialTheme.typography.labelMedium,
                                color = PureGreenPrimary
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rationPackages.forEach { product ->
                            val cartItem = uiState.cartItems.firstOrNull { it.productId == product.id }
                            ProductCard(
                                product = product,
                                onProductClick = { viewModel.selectProduct(product.id) },
                                onAddToCart = { viewModel.quickAddToCart(product) },
                                quantityInCart = cartItem?.quantity ?: 0
                            )
                        }
                    }
                }
            }
        }

        // 7. Popular Essentials (روزمرہ کی ضروری اشیاء: آٹا، گھی، چاول، دالیں)
        val popularEssentials = uiState.allProducts.filter { it.isFeatured && !it.isRationPackage }
        item {
            Column(modifier = Modifier.padding(top = 18.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "روزمرہ اشیائے ضروریہ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PureTextPrimary
                    )
                    TextButton(
                        onClick = {
                            viewModel.selectCategory(null)
                            viewModel.navigateTo(Screen.ProductList())
                        }
                    ) {
                        Text(
                            text = "سب اشیاء دیکھیں",
                            style = MaterialTheme.typography.labelMedium,
                            color = PureGreenPrimary
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    popularEssentials.forEach { product ->
                        val cartItem = uiState.cartItems.firstOrNull { it.productId == product.id }
                        ProductCard(
                            product = product,
                            onProductClick = { viewModel.selectProduct(product.id) },
                            onAddToCart = { viewModel.quickAddToCart(product) },
                            quantityInCart = cartItem?.quantity ?: 0
                        )
                    }
                }
            }
        }

        // 8. Pure Food Lodhran Trust Commitments
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PureSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "پیور فوڈ لودھراں کا عزم اور سہولیات",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PureGreenPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = PureGreenPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "100% خالص، بغیر ملاوٹ اور منتخب شدہ زرعی اجناس",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = PureGreenPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "لودھراں کے تمام محلوں اور روڈز پر اسی دن ہوم ڈیلیوری",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Handshake, contentDescription = null, tint = PureGreenPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "کیش آن ڈیلیوری: سودا وصول کر کے تسلی کے بعد ادائیگی کریں",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureTextSecondary
                        )
                    }
                }
            }
        }
    }
}
