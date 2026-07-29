package com.example.shopmini.data.model

import kotlinx.serialization.Serializable

//Yorumları karşılayan veri sınıfı
@Serializable
data class Review (
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)
