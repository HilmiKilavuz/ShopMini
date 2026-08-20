package com.example.shopmini.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.usecase.cart.DeleteItemUseCase
import com.example.shopmini.domain.usecase.cart.GetCartItemsUseCase
import com.example.shopmini.domain.usecase.cart.InsertItemUseCase
import com.example.shopmini.domain.usecase.cart.UpdateCartQuantityUseCase
import com.example.shopmini.domain.usecase.favorite.CheckIfFavoriteUseCase
import com.example.shopmini.domain.usecase.favorite.ToggleFavoriteUseCase
import com.example.shopmini.domain.usecase.product.GetProductDetailUseCase
import com.example.shopmini.ui.util.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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
    private val insertItemUseCase: InsertItemUseCase,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val analyticsManager: AnalyticsManager,
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

    // Bu ürünün sepetteki mevcut CartEntity'sini tutar. null ise sepette yok.
    val cartItem: StateFlow<CartEntity?> = savedStateHandle.get<Int>("productId")?.let { id ->
        getCartItemsUseCase()
            .map { items -> items.find { it.id == id } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    } ?: MutableStateFlow(null)


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
                analyticsManager.logViewItem(product)
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

    // Ürünü sepete eklemek için kullanılan fonksiyon
    fun insertCart(product: Product) {
        viewModelScope.launch {
            insertItemUseCase(product)
            analyticsManager.logAddToCart(product)
        }


    }

    // Adet arttırma işlemi için kullanılan fonksiyon
    fun increaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            updateCartQuantityUseCase(item.id, item.quantity + 1)
        }
    }

    // Adet azaltma işlemi için kullanılan fonksiyon
    fun decreaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            if (item.quantity == 1) deleteItemUseCase(item)
            else updateCartQuantityUseCase(item.id, item.quantity - 1)
        }
    }

}