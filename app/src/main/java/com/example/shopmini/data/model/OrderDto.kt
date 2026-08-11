package com.example.shopmini.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val user_id: String,
    val items: List<OrderItemDto>,
    val total_amount: Double,
    val created_at: String? = null
)

@Serializable
data class OrderItemDto(
    val product_id: Int,
    val title: String,
    val quantity: Int,
    val price: Double
)
