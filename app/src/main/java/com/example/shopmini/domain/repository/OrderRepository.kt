package com.example.shopmini.domain.repository

import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.domain.model.Order
import kotlinx.coroutines.flow.Flow


//Sipariş işlemleri için gerekli olan interface'ler
interface OrderRepository {
    suspend fun saveOrder(cartItems: List<CartEntity>, totalAmount: Double): Result<Unit>
     fun getOrders(): Flow<List<Order>>
}