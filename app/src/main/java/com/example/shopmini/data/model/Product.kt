package com.example.shopmini.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val discountPercentage : Double,
    val category:String
)