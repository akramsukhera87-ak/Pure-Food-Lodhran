package com.example.data.model

data class OrderItem(
    val productId: String,
    val nameUrdu: String,
    val selectedUnit: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double
)

data class Order(
    val orderId: String,
    val customerName: String,
    val customerPhone: String,
    val lodhranArea: String,
    val addressDetail: String,
    val deliveryNotes: String,
    val subtotal: Double,
    val deliveryFee: Double,
    val grandTotal: Double,
    val paymentMethod: String = "کیش آن ڈیلیوری (نقد رقم بوقت وصولی)",
    val timestamp: Long = System.currentTimeMillis(),
    val statusUrdu: String = "موصول ہو گیا",
    val isDemo: Boolean = true,
    val items: List<OrderItem> = emptyList()
)

object LodhranAreas {
    val ALL_AREAS = listOf(
        "لودھراں سٹی / پرانا لودھراں",
        "ریلوے روڈ و ریلوے کالونی",
        "کہروڑ پکا روڈ و ملحقہ آبادیاں",
        "دنیا پور روڈ و چوک قریشی",
        "غلہ منڈی، صرافہ بازار و مین بازار",
        "سپر ہائی وے / بائی پاس لودھراں",
        "فتح پور روڈ و ماڈل ٹاؤن کالونی",
        "موضع خانپور و قریبی مضافات"
    )
}

object DeliveryConfig {
    const val FREE_DELIVERY_THRESHOLD = 2000.0 // PKR
    const val STANDARD_DELIVERY_FEE = 150.0 // PKR
    const val STORE_PHONE = "0300-1234567"
    const val STORE_WHATSAPP = "923001234567"
    const val STORE_ADDRESS = "پیور فوڈ، نزد پرانی غلہ منڈی، ریلوے روڈ، لودھراں، پنجاب"
}
