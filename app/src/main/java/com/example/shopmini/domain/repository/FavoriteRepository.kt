package com.example.shopmini.domain.repository

import com.example.shopmini.data.local.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<FavoriteEntity>>
    suspend fun insertFavorite(favorite: FavoriteEntity)
    suspend fun deleteFavorite(favorite: FavoriteEntity)
    fun isFavorite(productId: Int): Flow<Boolean>
}
