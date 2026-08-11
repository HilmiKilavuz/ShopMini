package com.example.shopmini.domain.usecase.order

import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.domain.repository.OrderRepository
import javax.inject.Inject

class SaveOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository

) {
    suspend operator fun invoke(
        cartItems: List<CartEntity>,
        totalAmount: Double
    ): Result<Unit> {
        return orderRepository.saveOrder(cartItems,totalAmount)
    }

}

