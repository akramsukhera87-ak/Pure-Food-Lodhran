package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.DeliveryConfig
import com.example.data.model.GroceryCategories
import com.example.data.model.SampleProducts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Pure Food", appName)
    }

    @Test
    fun `verify sample products and categories are loaded`() {
        assertTrue(SampleProducts.PRODUCTS.isNotEmpty())
        assertTrue(GroceryCategories.ALL_CATEGORIES.isNotEmpty())

        val atta = SampleProducts.PRODUCTS.firstOrNull { it.id == "prod_atta_chakki" }
        assertNotNull(atta)
        assertEquals("چکی کا خالص دیسی آٹا", atta?.nameUrdu)
    }

    @Test
    fun `verify delivery config threshold and phone regex`() {
        assertEquals(2000.0, DeliveryConfig.FREE_DELIVERY_THRESHOLD, 0.01)

        val validPhone = "03001234567"
        val invalidPhone = "04001234567"
        val phoneRegex = Regex("^03\\d{9}$")

        assertTrue(validPhone.matches(phoneRegex))
        assertTrue(!invalidPhone.matches(phoneRegex))
    }
}
