package com.example.shopmini.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.usecase.favorite.GetFavoritesUseCase
import com.example.shopmini.domain.usecase.order.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Siparişleri çek ve hesapla
                getOrdersUseCase().collect { orders ->
                    val grouped = orders
                        .filter { it.createdAt.length >= 7 }   // boş veya kısa tarihleri atla
                        .groupBy { it.createdAt.substring(0, 7)  }

                    val monthlySpending = grouped.mapValues { (_, list) ->
                        list.sumOf { it.totalAmount }
                    }
                    val monthlyOrderCount = grouped.mapValues { (_, list) ->
                        list.size
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        monthlySpending = monthlySpending,
                        monthlyOrderCount = monthlyOrderCount,
                        totalSpent = orders.sumOf { it.totalAmount },
                        totalOrders = orders.size
                    )
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "İstatistikler yüklenemedi."
                )
            }


        }

        viewModelScope.launch {
            try {
                // Favorileri de çek
                getFavoritesUseCase().collect { favorites ->
                    _uiState.value = _uiState.value.copy(
                        topFavorites = favorites.take(10)  // ilk 10'u al
                    )
                }

            }catch (e:Exception){
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "İstatistikler yüklenemedi."
                )

            }
        }
    }

}