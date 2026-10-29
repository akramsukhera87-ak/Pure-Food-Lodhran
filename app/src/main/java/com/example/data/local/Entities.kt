package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String, // composite id: "${productId}_${selectedUnit}"
    val productId: String,
    val nameUrdu: String,
    val nameEnglish: String,
    val categoryId: String,
    val selectedUnit: String,
    val unitPrice: Double,
    val quantity: Int
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val customerName: String,
    val customerPhone: String,
    val lodhranArea: String,
    val addressDetail: String,
    val deliveryNotes: String,
    val subtotal: Double,
    val deliveryFee: Double,
    val grandTotal: Double,
    val paymentMethod: String,
    val timestamp: Long,
    val statusUrdu: String,
    val isDemo: Boolean
)

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: String,
    val productId: String,
    val nameUrdu: String,
    val selectedUnit: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double
)
