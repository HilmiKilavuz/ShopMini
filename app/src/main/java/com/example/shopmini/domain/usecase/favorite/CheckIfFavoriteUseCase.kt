package com.example.shopmini.domain.usecase.favorite


import com.example.shopmini.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//Ürünün favorilere eklenmiş mi kontrolünü yapan usecase sınıfı
class CheckIfFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    // Burada suspend yok çünkü Flow dönüyoruz, anlık veri dinleyeceğiz
    operator fun invoke(productId: Int): Flow<Boolean> {
        return favoriteRepository.isFavorite(productId)
    }
}
