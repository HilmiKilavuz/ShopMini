package com.example.shopmini.data.model

import kotlinx.serialization.Serializable

//Yorumların tutulduğu sınıf
@Serializable
data class Review (
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)
