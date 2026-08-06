package com.example.shopmini.ui.screens.profile.address

import com.example.shopmini.domain.model.Address


sealed class AddressesUiState {
    object Loading : AddressesUiState()
    data class Success(val addresses: List<Address>) : AddressesUiState()
    data class Error(val message: String) : AddressesUiState()
}