package com.example.shopmini.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.local.FavoriteEntity
import com.example.shopmini.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()


init {
    loadFavorites()
}

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoriteUiState.Loading
            try {

                favoriteRepository.getFavorites().collect {favoriteList->
                    _uiState.value= FavoriteUiState.Success(favoriteList)

                }
            }
            catch (e: Exception) {
                _uiState.value = FavoriteUiState.Error(e.message ?: "Unknown error")
            }
        }

    }



    fun deleteFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(favorite)
        }
    }

}