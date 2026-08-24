package com.example.shopmini.ui.screens.cart

import com.example.shopmini.data.local.entity.CartEntity



    // Veri yüklenirken gösterilecek durum
    data class CartUiState(
        val isLoading: Boolean = false,
        val cartItems: List<CartEntity> = emptyList(),
        val subtotal: Double = 0.0,
        val discountTotal: Double = 0.0,
        val grandTotal: Double = 0.0,
        val error: String? = null,
        val navigateToLogin: Boolean = false,
        val navigateToCheckout: Boolean = false
    )
