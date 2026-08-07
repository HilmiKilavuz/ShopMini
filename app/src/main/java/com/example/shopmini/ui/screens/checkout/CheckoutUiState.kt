package com.example.shopmini.ui.screens.checkout

import com.example.shopmini.domain.model.Address

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val addresses: List<Address> = emptyList(),
    val selectedAddress: Address? = null,
    val error: String? = null
)