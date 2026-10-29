package com.example.data.model

data class CartItem(
    val id: String, // composite id: "${productId}_${selectedUnit}"
    val productId: String,
    val nameUrdu: String,
    val nameEnglish: String,
    val categoryId: String,
    val selectedUnit: String,
    val unitPrice: Double,
    val quantity: Int
) {
    val totalPrice: Double
        get() = unitPrice * quantity
}
