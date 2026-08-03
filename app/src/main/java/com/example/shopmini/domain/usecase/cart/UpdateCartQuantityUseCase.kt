package com.example.shopmini.domain.usecase.cart

import com.example.shopmini.domain.repository.CartRepository
import javax.inject.Inject
//Sepetteki ürünleri günceller
//Sepetteki ürünlerin adedini günceller
 class UpdateCartQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: Int, newQuantity: Int) {
        cartRepository.updateQuantity(productId, newQuantity)
    }

}