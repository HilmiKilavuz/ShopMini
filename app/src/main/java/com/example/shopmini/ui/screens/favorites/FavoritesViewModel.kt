package com.example.shopmini.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.data.local.entity.FavoriteEntity
import com.example.shopmini.domain.usecase.favorite.DeleteFavoriteUseCase
import com.example.shopmini.domain.usecase.favorite.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
//Favoriler için oluşturulmuş ViewModel
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {
    val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()


    init {
        loadFavorites()
    }
//Favorileri yüklemek için kullanılan fonksiyon
    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoriteUiState.Loading
            try {

                getFavoritesUseCase().collect { favoriteList ->
                    _uiState.value = FavoriteUiState.Success(favoriteList)

                }
            } catch (e: Exception) {
                _uiState.value = FavoriteUiState.Error(e.message ?: "Unknown error")
            }
        }

    }

//Favorileri silmek için kullanılan fonksiyon
    fun deleteFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            deleteFavoriteUseCase(favorite)
        }
    }

}