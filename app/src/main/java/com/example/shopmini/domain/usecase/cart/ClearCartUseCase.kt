package com.example.shopmini.domain.usecase.cart

import com.example.shopmini.domain.repository.CartRepository
import javax.inject.Inject


class ClearCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke() {
        cartRepository.clearCart()
    }
}