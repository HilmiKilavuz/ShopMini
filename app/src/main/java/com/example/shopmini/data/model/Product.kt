/**
 * DATA Katmanı.
 * Ürün verisini tutan ana modeldir. (API'den veya Local DB'den geldiğinde bu modele dönüşür).
 */
package com.example.shopmini.data.model

import kotlinx.serialization.Serializable
//Product verisini karşılayan veri sınıfı
@Serializable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val discountPercentage : Double,
    val category:String,
    val reviews: List<Review>?=null,
    val stock: Int
)
