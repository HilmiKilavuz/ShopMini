package com.example.shopmini.ui.screens.payment


data class PaymentUiState (
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
    val isLoading: Boolean = false

)