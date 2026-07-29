package com.example.shopmini.ui.screens.favorites

import com.example.shopmini.data.local.FavoriteEntity
//Favoriler için oluşturulmuş UIState bileşenidir.
sealed class FavoriteUiState {

    object Loading : FavoriteUiState()
    data class Success(val favorites: List<FavoriteEntity>) : FavoriteUiState()
    data class Error(val message: String) : FavoriteUiState()
}
