package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PureGreenPrimary
import com.example.ui.theme.PureSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PureFoodTopBar(
    titleUrdu: String = "پیور فوڈ لودھراں",
    subtitleUrdu: String = "خالص راشن و گھریلو سودا سلف",
    canNavigateBack: Boolean = false,
    onNavigateBack: () -> Unit = {},
    cartItemCount: Int = 0,
    onCartClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_purefood_logo),
                    contentDescription = "پیور فوڈ لوگو",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = titleUrdu,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PureSurface
                    )
                    Text(
                        text = subtitleUrdu,
                        style = MaterialTheme.typography.labelSmall,
                        color = PureSurface.copy(alpha = 0.85f),
                        fontSize = 11.sp
                    )
                }
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "واپس جائیں",
                        tint = PureSurface
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onAboutClick,
                modifier = Modifier.testTag("top_bar_about_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "ہمارے متعلق",
                    tint = PureSurface
                )
            }

            IconButton(
                onClick = onCartClick,
                modifier = Modifier.testTag("top_bar_cart_button")
            ) {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
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
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "شاپنگ کارٹ",
                        tint = PureSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PureGreenPrimary,
            titleContentColor = PureSurface
        )
    )
}
