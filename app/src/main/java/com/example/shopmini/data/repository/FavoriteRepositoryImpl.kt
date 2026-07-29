package com.example.shopmini.data.repository

import com.example.shopmini.data.local.FavoriteDao
import com.example.shopmini.data.local.FavoriteEntity
import com.example.shopmini.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//Favoriler için repository sınıfı
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {
    override fun getFavorites(): Flow<List<FavoriteEntity>> {
       return favoriteDao.getFavorites()
    }

    override suspend fun insertFavorite(favorite: FavoriteEntity) {
        favoriteDao.insertFavorite(favorite)

    }

    override suspend fun deleteFavorite(favorite: FavoriteEntity) {
        favoriteDao.deleteFavorite(favorite)
    }

    override fun isFavorite(productId: Int): Flow<Boolean> {
        return favoriteDao.isFavorite(productId)
    }
}