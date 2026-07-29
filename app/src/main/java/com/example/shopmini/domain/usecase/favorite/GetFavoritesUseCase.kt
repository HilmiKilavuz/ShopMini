package com.example.shopmini.domain.usecase.favorite

import com.example.shopmini.data.local.FavoriteEntity
import com.example.shopmini.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//Favorilerden ürünleri getirmek için GetFavoritesUseCase sınıfı
class GetFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
){

     operator fun invoke(): Flow<List<FavoriteEntity>>{
        return favoriteRepository.getFavorites()
    }
}