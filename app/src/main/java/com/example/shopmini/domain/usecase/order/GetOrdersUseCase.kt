package com.example.shopmini.domain.usecase.order

import com.example.shopmini.domain.model.Order
import com.example.shopmini.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    operator fun invoke() : Flow<List<Order>>{
        return orderRepository.getOrders()

    }

}