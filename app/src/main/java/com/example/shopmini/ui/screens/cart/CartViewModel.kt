package com.example.shopmini.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.domain.usecase.auth.IsUserLoggedInUseCase
import com.example.shopmini.domain.usecase.cart.DeleteItemUseCase
import com.example.shopmini.domain.usecase.cart.GetCartItemsUseCase
import com.example.shopmini.domain.usecase.cart.UpdateCartQuantityUseCase
import com.example.shopmini.ui.util.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//Sepet sisteminin viewmodel'i
@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val deleteCartItemUseCase: DeleteItemUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val analyticsManager: AnalyticsManager,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        getCart()
    }

//Sepetteki ürünleri getirme işlemi
    private fun getCart() {

        viewModelScope.launch {

            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true
                )
                getCartItemsUseCase().collect { items ->
                    _uiState.value = _uiState.value.copy(
                        cartItems = items,
                        isLoading = false
                    )
                    calculateTotal(items)

                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message.toString()
                )

            }


        }
    }
//Toplam hesaplama işlemi
    private fun calculateTotal(items: List<CartEntity>) {
        // Her ürünün fiyatı
        val subtotal = items.sumOf { it.price * it.quantity }

        // Her ürünün indirimli tutarı
        val discountTotal = items.sumOf { it.price * it.quantity * (it.discountPercentage / 100) }

        // Ödenecek tutar
        val grandTotal = subtotal - discountTotal

        _uiState.value = _uiState.value.copy(
            subtotal = subtotal,
            discountTotal = discountTotal,
            grandTotal = grandTotal
        )
    }
    // Adet arttırma işlemi
    fun increaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            updateCartQuantityUseCase(item.id, item.quantity + 1)

        }

    }
//Adet azaltma işlemi
    fun decreaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            if (item.quantity == 1) {
                deleteCartItemUseCase(item)
            } else {
                updateCartQuantityUseCase(item.id, item.quantity - 1)

            }

        }
    }
//Sepetten silme işlemi
    fun deleteCartItem(item: CartEntity) {
        viewModelScope.launch {
            deleteCartItemUseCase(item)
        }

    }

    fun onCheckoutClicked() {
        if (isUserLoggedInUseCase()) {
            // Giriş yapılmış → analytics logla, checkout'a geç
            analyticsManager.logBeginCheckout(uiState.value.grandTotal)
            _uiState.value = _uiState.value.copy(navigateToCheckout = true)
        } else {
            // Giriş yapılmamış → login ekranına yönlendir
            _uiState.value = _uiState.value.copy(navigateToLogin = true)
        }
    }

    // Login navigasyonu tamamlandığında state'i sıfırla
    fun onNavigateToLoginHandled() {
        _uiState.value = _uiState.value.copy(navigateToLogin = false)
    }

    fun onNavigateToCheckoutHandled() {
        _uiState.value = _uiState.value.copy(navigateToCheckout = false)
    }



}