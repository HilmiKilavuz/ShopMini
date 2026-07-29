package com.example.shopmini.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.usecase.favorite.CheckIfFavoriteUseCase
import com.example.shopmini.domain.usecase.favorite.ToggleFavoriteUseCase
import com.example.shopmini.domain.usecase.product.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Ürün Detay Ekranının beynidir.
 * Ekranın ihtiyaç duyduğu veriyi Repository'den çeker ve UiState üzerinden ekrana sunar.
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(

    //Use Caseler ekleyerek tek bir işlevi yerine getiren kodlar yazıyoruz
    // ve işlemlerini viewmodelden ayrıştırıyoruz.
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val checkIfFavoriteUseCase: CheckIfFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,

    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Ekranın dinleyeceği değişken durum
    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // Ürün favorilere eklenmiş mi kontrolü
    val isFavorite: StateFlow<Boolean> = savedStateHandle.get<Int>("productId")?.let { id ->
        checkIfFavoriteUseCase(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    } ?: MutableStateFlow(false)


    init {
        // Navigasyondan gelen "productId" değerini alıyoruz
        val productId = savedStateHandle.get<Int>("productId")
        if (productId != null) {
            loadProductDetail(productId)
        } else {
            _uiState.value = ProductDetailUiState.Error("Product Not Found")
        }

    }

    // Veriyi yüklemek için kullanılan fonksiyon
    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            _isRefreshing.value = true
            try {
                val product = getProductDetailUseCase(id)
                _uiState.value = ProductDetailUiState.Success(product)
            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState.Error(e.message ?: "Unknown error")
            } finally {
                _isRefreshing.value = false
            }

        }


    }
// Ürünü favorilere eklemek için kullanılan fonksiyon
    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            toggleFavoriteUseCase(product, isFavorite.value)
        }


    }
}