/**
 * UI Katmanı.
 * Ana ekranın anlık durumunu tutan sınıftır.
 */
package com.example.shopmini.ui.screens.home

import com.example.shopmini.data.model.Product
//Ana ekran için oluşturulmuş UIState bileşenidir.
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val products: List<Product>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
