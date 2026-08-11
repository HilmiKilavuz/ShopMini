package com.example.shopmini.ui.screens.profile.orders

import com.example.shopmini.domain.model.Order


data class OrdersUiState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val error: String? = null
)