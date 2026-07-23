package com.example.shopmini.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Ürün Detay Ekranının beynidir.
 * Ekranın ihtiyaç duyduğu veriyi Repository'den çeker ve UiState üzerinden ekrana sunar.
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    // Navigasyon sırasında gönderilen parametreleri havada yakalamamızı sağlar
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Ekranın dinleyeceği değişken durum (State)
    private val _uiState= MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        // Navigasyondan gelen "productId" değerini alıyoruz
        val productId = savedStateHandle.get<Int>("productId")
        if (productId != null) {
            loadProductDetail(productId)
        }else{
            _uiState.value=ProductDetailUiState.Error("Product Not Found")
        }

    }
    fun loadProductDetail(id :Int){
        viewModelScope.launch{
            _uiState.value = ProductDetailUiState.Loading
            try {
                val product = productRepository.getProductById(id)
                _uiState.value= ProductDetailUiState.Success(product)
            }catch (e: Exception){
                _uiState.value = ProductDetailUiState.Error(e.message ?: "Unknown error")
            }

        }


    }
}