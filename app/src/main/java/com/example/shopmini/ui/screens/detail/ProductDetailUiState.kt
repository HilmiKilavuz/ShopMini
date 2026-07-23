package com.example.shopmini.ui.screens.detail

import com.example.shopmini.data.model.Product

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()


}