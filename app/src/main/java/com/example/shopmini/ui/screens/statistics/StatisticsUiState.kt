package com.example.shopmini.ui.screens.statistics

import com.example.shopmini.data.local.entity.FavoriteEntity

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Harcama grafiği için: Ay → Toplam TL
    val monthlySpending: Map<String, Double> = emptyMap(),

    // Sipariş zaman çizelgesi için: Ay → Sipariş sayısı
    val monthlyOrderCount: Map<String, Int> = emptyMap(),

    // Toplam özet
    val totalSpent: Double = 0.0,
    val totalOrders: Int = 0,

    // Favoriler
    val topFavorites: List<FavoriteEntity> = emptyList()
)
