package com.example.shopmini.domain.usecase.cart

import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.domain.repository.CartRepository
import javax.inject.Inject
//Sepetten ürün silme işlemi
 class DeleteItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(item: CartEntity){
        cartRepository.deleteItem(item)
    }
}