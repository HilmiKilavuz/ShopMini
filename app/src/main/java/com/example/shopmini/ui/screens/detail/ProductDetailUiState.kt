package com.example.shopmini.ui.screens.detail

import com.example.shopmini.data.model.Product

/**
 * Ürün Detay sayfasının anlık durumlarını (State) temsil eder.
 * Ekran (UI) bu durumlara bakarak kendini çizer.
 */
sealed class ProductDetailUiState {
    // Veri yüklenirken gösterilecek durum (Örn: Dönüşen yuvarlak ikon)
    object Loading : ProductDetailUiState()
    
    // Veri başarıyla çekildiğinde ürün bilgilerini tutan durum
    data class Success(val product: Product) : ProductDetailUiState()
    
    // Bir hata oluştuğunda hata mesajını tutan durum
    data class Error(val message: String) : ProductDetailUiState()


}