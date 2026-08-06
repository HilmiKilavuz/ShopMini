package com.example.shopmini.ui.screens.profile.address

import com.example.shopmini.domain.model.Address

sealed class AddEditAddressUiState {

    object Loading : AddEditAddressUiState()
    object Success : AddEditAddressUiState()
    data class Error(val message: String) : AddEditAddressUiState()

}