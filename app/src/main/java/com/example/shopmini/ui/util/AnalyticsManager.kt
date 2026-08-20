package com.example.shopmini.ui.util

import android.os.Bundle
import com.example.shopmini.data.model.Product
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {
    object Events {
        const val VIEW_ITEM = "view_item"
        const val ADD_TO_CART = "add_to_cart"
        const val BEGIN_CHECKOUT = "begin_checkout"
        const val ADD_SHIPPING_INFO = "add_shipping_info"
        const val PURCHASE = "purchase"
    }

    object Params {
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val ITEM_CATEGORY = "item_category"
        const val PRICE = "price"
        const val QUANTITY = "quantity"
        const val VALUE = "value"
        const val CURRENCY = "currency"
        const val TRANSACTION_ID = "transaction_id"
        const val ITEMS          = "items"
    }

    fun logViewItem(product: Product) {
        val bundle = Bundle().apply {
            putString(Params.ITEM_ID, product.id.toString())
            putString(Params.ITEM_NAME, product.title)
            putString(Params.ITEM_CATEGORY, product.category)
            putDouble(Params.PRICE, product.price)
            putString(Params.CURRENCY, "USD")
        }
        firebaseAnalytics.logEvent(Events.VIEW_ITEM, bundle)
    }

    fun logAddToCart(product: Product) {
        val itemBundle = Bundle().apply {
            putString(Params.ITEM_ID, product.id.toString())
            putString(Params.ITEM_NAME, product.title)
            putString(Params.ITEM_CATEGORY, product.category)
            putDouble(Params.PRICE, product.price)
            putInt(Params.QUANTITY, 1)
        }
        val bundle = Bundle().apply {
            putString(Params.CURRENCY, "USD")
            putDouble(Params.VALUE, product.price)      // toplam değer
            putParcelableArrayList(Params.ITEMS, arrayListOf(itemBundle))  // ürün listesi
        }
        firebaseAnalytics.logEvent(Events.ADD_TO_CART, bundle)

    }

    fun logBeginCheckout(totalAmount: Double) {
        val bundle = Bundle().apply {
            putDouble(Params.VALUE, totalAmount)
            putString(Params.CURRENCY, "USD")
        }
        firebaseAnalytics.logEvent(Events.BEGIN_CHECKOUT, bundle)
    }

    fun logAddShippingInfo() {
        val bundle = Bundle().apply {
            putString(Params.CURRENCY, "USD")
        }
        firebaseAnalytics.logEvent(Events.ADD_SHIPPING_INFO, bundle)
    }


    fun logPurchase(totalAmount: Double, orderId: String) {
        val bundle = Bundle().apply {
            putString(Params.TRANSACTION_ID, orderId)   // ← zorunlu!
            putDouble(Params.VALUE, totalAmount)
            putString(Params.CURRENCY, "USD")
        }
        firebaseAnalytics.logEvent(Events.PURCHASE, bundle)
    }

}