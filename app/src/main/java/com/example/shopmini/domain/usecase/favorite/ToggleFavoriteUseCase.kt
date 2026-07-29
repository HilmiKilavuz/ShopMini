package com.example.shopmini.domain.usecase.favorite

import com.example.shopmini.data.local.FavoriteEntity
import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.repository.FavoriteRepository
import javax.inject.Inject

//Ürünün favorilerde olup olmadığını kontrol etmek için ToggleFavoriteUseCase sınıfı
class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(product: Product, isCurrentlyFavorite: Boolean) {
        val favorite = FavoriteEntity(
            id = product.id,
            title = product.title,
            price = product.price,
            thumbnail = product.thumbnail,
            discountPercentage = product.discountPercentage
        )

        if (isCurrentlyFavorite) {
            favoriteRepository.deleteFavorite(favorite)
        } else {
            favoriteRepository.insertFavorite(favorite)
        }
    }
}