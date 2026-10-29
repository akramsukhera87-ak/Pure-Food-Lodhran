package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.OilBarrel
import androidx.compose.material.icons.filled.RiceBowl
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val nameUrdu: String,
    val nameEnglish: String,
    val descriptionUrdu: String,
    val icon: ImageVector,
    val itemCount: Int = 0
)

object GroceryCategories {
    val ALL_CATEGORIES = listOf(
        Category(
            id = "cat_flour",
            nameUrdu = "آٹا اور اناج",
            nameEnglish = "Atta & Flour",
            descriptionUrdu = "چکی کا تازہ آٹا، خالص گندم اور فائن میدہ",
            icon = Icons.Default.Grass,
            itemCount = 4
        ),
        Category(
            id = "cat_oil_ghee",
            nameUrdu = "گھی اور کوکنگ آئل",
            nameEnglish = "Ghee & Oil",
            descriptionUrdu = "خالص دیسی گھی، سرسوں کا تیل اور برانڈڈ آئل",
            icon = Icons.Default.OilBarrel,
            itemCount = 4
        ),
        Category(
            id = "cat_rice",
            nameUrdu = "باسمتی و سیلا چاول",
            nameEnglish = "Rice",
            descriptionUrdu = "پریمیئم سپر باسمتی پرانا چاول اور سیلا چاول",
            icon = Icons.Default.RiceBowl,
            itemCount = 3
        ),
        Category(
            id = "cat_pulses",
            nameUrdu = "دالیں اور چنے",
            nameEnglish = "Pulses & Dal",
            descriptionUrdu = "دال چنا، دال مسور، دال مونگ، دال ماش اور سفید چنے",
            icon = Icons.Default.Spa,
            itemCount = 5
        ),
        Category(
            id = "cat_sugar_tea",
            nameUrdu = "چینی، چائے اور نمک",
            nameEnglish = "Sugar, Tea & Salt",
            descriptionUrdu = "صاف دانے دار چینی، کینیا چائے اور لاہوری نمک",
            icon = Icons.Default.LocalDrink,
            itemCount = 3
        ),
        Category(
            id = "cat_spices",
            nameUrdu = "خالص مصالحہ جات",
            nameEnglish = "Spices & Haldi",
            descriptionUrdu = "خالص ہلدی، لال مرچ، دھنیا پاؤڈر اور گرم مصالحہ",
            icon = Icons.Default.Fastfood,
            itemCount = 4
        ),
        Category(
            id = "cat_ration",
            nameUrdu = "ماہانہ راشن پیکجز",
            nameEnglish = "Monthly Ration Bundles",
            descriptionUrdu = "پورے مہینے کا کفایتی مکمل فیملی راشن پیکج",
            icon = Icons.Default.Inventory2,
            itemCount = 2
        ),
        Category(
            id = "cat_household",
            nameUrdu = "گھریلو صفائی اور صابن",
            nameEnglish = "Household Essentials",
            descriptionUrdu = "سرف پاؤڈر، برتن دھونے کا صابن اور نہانے کے صابن",
            icon = Icons.Default.CleaningServices,
            itemCount = 3
        )
    )
}
