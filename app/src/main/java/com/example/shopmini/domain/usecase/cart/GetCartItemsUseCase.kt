package com.example.shopmini.domain.usecase.cart

import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//Sepetten ürünleri getirir
class GetCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<List<CartEntity>> {
        return cartRepository.getCartItems()

    }
}