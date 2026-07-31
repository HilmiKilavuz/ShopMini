package com.example.shopmini.domain.usecase.cart

import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.repository.CartRepository
import javax.inject.Inject

 class InsertItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(product: Product){
        val cartItem = CartEntity(
            id = product.id,
            title = product.title,
            price = product.price,
            quantity = 1,
            thumbnail = product.thumbnail,
            discountPercentage = product.discountPercentage
        )
        cartRepository.insertItem(cartItem)
    }
}