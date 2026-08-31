package com.example.shopmini.ui.screens.payment

import com.example.shopmini.domain.model.Coupon

data class PaymentUiState(
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val expiryDate: String = "",   // "MM/YY" formatında
    val cvv: String = "",
    // Hata mesajları — null ise hata yok
    val cardNumberError: String? = null,
    val cardHolderNameError: String? = null,
    val expiryDateError: String? = null,
    val cvvError: String? = null,
    val isPaymentSuccessful: Boolean = false,
    val isLoading: Boolean = false,

    // --- Kupon alanları ---
    val couponCode: String = "",
    val couponError: String? = null,
    val isValidatingCoupon: Boolean = false,
    val appliedCoupon: Coupon? = null,

    // --- Tutar özeti ---
    val subtotal: Double = 0.0,            // Ürünlerin ham toplamı (indirim öncesi)
    val productDiscount: Double = 0.0,     // Ürün bazlı indirimler toplamı
    val couponDiscountAmount: Double = 0.0,// Kupon ile sağlanan indirim tutarı (₺)
    val finalAmount: Double = 0.0          // Ödenecek nihai tutar
)