package com.example.shopmini.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Home

    @Serializable
    data class ProductDetailScreen(val productId: Int)

}