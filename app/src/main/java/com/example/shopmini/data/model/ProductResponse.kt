package com.example.shopmini.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val products: List<Product>
)