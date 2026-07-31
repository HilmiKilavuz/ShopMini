package com.example.shopmini.domain.usecase.favorite

import com.example.shopmini.data.local.entity.FavoriteEntity
import com.example.shopmini.domain.repository.FavoriteRepository
import javax.inject.Inject
//Ürün favorilerden çıkarılmasını yapan usecase sınıfı
class DeleteFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
){
    suspend operator fun invoke(favorite: FavoriteEntity) {
        favoriteRepository.deleteFavorite(favorite)
    }


}