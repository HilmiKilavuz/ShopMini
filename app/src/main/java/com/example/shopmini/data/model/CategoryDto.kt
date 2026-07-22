package com.example.shopmini.data.model

import kotlinx.serialization.Serializable


@Serializable
data class CategoryDto(
    val slug: String,   // URL'de kullanılan kısa isim (örn: "smartphones")
    val name: String,   // Görünen isim (örn: "Smartphones")
    val url: String     // Tam API adresi (bize lazım olmayacak ama API gönderiyor)
)

