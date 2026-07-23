/**
 * DATA Katmanı.
 * API'den dönecek Kategori verisini karşılayar.
 */
package com.example.shopmini.data.model

import kotlinx.serialization.Serializable


@Serializable
data class CategoryDto(
    val slug: String,
    val name: String,
    val url: String
)
