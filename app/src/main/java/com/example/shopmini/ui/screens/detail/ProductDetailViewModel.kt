package com.example.shopmini.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.local.FavoriteEntity
import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.repository.FavoriteRepository
import com.example.shopmini.domain.repository.ProductRepository
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
    private val productRepository: ProductRepository,
    // Navigasyon sırasında gönderilen parametreleri havada yakalamamızı sağlar
    savedStateHandle: SavedStateHandle,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    // Ekranın dinleyeceği değişken durum
    private val _uiState= MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // Ürün favorilere eklenmiş mi kontrolü
    val isFavorite: StateFlow<Boolean> = savedStateHandle.get<Int>("productId")?.let { id ->
        favoriteRepository.isFavorite(id).stateIn(
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
        }else{
            _uiState.value=ProductDetailUiState.Error("Product Not Found")
        }

    }

    // Veriyi yüklemek için kullanılan fonksiyon
    fun loadProductDetail(id :Int){
        viewModelScope.launch{
            _uiState.value = ProductDetailUiState.Loading
            _isRefreshing.value = true
            try {
                val product = productRepository.getProductById(id)
                _uiState.value= ProductDetailUiState.Success(product)
            }catch (e: Exception){
                _uiState.value = ProductDetailUiState.Error(e.message ?: "Unknown error")
            }finally {
                _isRefreshing.value = false
            }

        }


    }

    fun toogleFavorite(product: Product){
        val favorite = FavoriteEntity(
            id = product.id,
            title = product.title,
            price = product.price,
            thumbnail = product.thumbnail,
            discountPercentage = product.discountPercentage
        )
        viewModelScope.launch {
           if(isFavorite.value){
               favoriteRepository.deleteFavorite(favorite)

           }else{
               favoriteRepository.insertFavorite(favorite)
           }

        }

    }
}