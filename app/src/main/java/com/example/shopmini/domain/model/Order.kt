package com.example.shopmini.domain.model


data class Order(
    val items: List<OrderItem>,
    val totalAmount: Double,
    val createdAt: String
)

data class OrderItem(
    val productId: Int,
    val title: String,
    val quantity: Int,
    val price: Double
)
