package com.example.shopmini.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.model.Address
import com.example.shopmini.domain.usecase.address.GetAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase

) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAddresses()

    }


     fun loadAddresses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getAddressesUseCase().catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect {list->
                val default = list.firstOrNull { it.isDefault } ?: list.firstOrNull()
                _uiState.update {
                    it.copy(isLoading = false, addresses = list, selectedAddress = default)
                }
            }

        }

    }
    fun onAddressSelected(address: Address) {
        _uiState.update { it.copy(selectedAddress = address) }
    }

}