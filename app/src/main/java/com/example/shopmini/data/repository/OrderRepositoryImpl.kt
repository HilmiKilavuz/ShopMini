package com.example.shopmini.data.repository

import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.data.model.OrderDto
import com.example.shopmini.data.model.OrderItemDto
import com.example.shopmini.domain.model.Order
import com.example.shopmini.domain.model.OrderItem
import com.example.shopmini.domain.repository.OrderRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val supaBase: SupabaseClient
) : OrderRepository {
    override suspend fun saveOrder(
        cartItems: List<CartEntity>,
        totalAmount: Double
    ): Result<Unit> = runCatching {
        val userId = supaBase.auth.currentUserOrNull()?.id
            ?: throw Exception("Kullanıcı bulunamadı, lütfen tekrar giriş yapın.")
        val orderItemsDto = cartItems.map { cartItem ->
            OrderItemDto(
                product_id = cartItem.id,
                title = cartItem.title,
                quantity = cartItem.quantity,
                price = cartItem.price
            )
        }
        val orderDto = OrderDto(
            user_id = userId,
            items = orderItemsDto,
            total_amount = totalAmount
            // created_at otomatik eklenecek
        )
        supaBase.postgrest["orders"].insert(orderDto)


    }

    override fun getOrders(): Flow<List<Order>> = flow {
        val userId = supaBase.auth.currentUserOrNull()?.id
            ?: throw Exception("Kullanıcı bulunamadı")
        val response = supaBase.postgrest["orders"]
            .select {
                filter { eq("user_id", userId) }
                order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
            }.decodeList<OrderDto>()
        val orders = response.map { dto ->
            Order(
                totalAmount = dto.total_amount,
                createdAt = dto.created_at ?: "",
                items = dto.items.map { itemDto ->
                    OrderItem(
                        productId = itemDto.product_id,
                        title = itemDto.title,
                        quantity = itemDto.quantity,
                        price = itemDto.price
                    )
                }
            )
        }
        emit(orders)
    }
}