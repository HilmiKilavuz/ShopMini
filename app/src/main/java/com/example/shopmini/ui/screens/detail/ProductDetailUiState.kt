package com.example.shopmini.ui.screens.detail

import com.example.shopmini.data.model.Product

/**
 * Ürün Detay sayfasının anlık durumlarını  temsil eder.
 * Ekran  bu durumlara bakarak kendini çizer.
 */
sealed class ProductDetailUiState {
    // Veri yüklenirken gösterilecek durum
    object Loading : ProductDetailUiState()
    
    // Veri başarıyla çekildiğinde ürün bilgilerini tutan durum
    data class Success(val product: Product) : ProductDetailUiState()
    
    // Bir hata oluştuğunda hata mesajını tutan durum
    data class Error(val message: String) : ProductDetailUiState()


}