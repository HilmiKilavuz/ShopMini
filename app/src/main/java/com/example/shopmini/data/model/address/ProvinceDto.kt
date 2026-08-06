package com.example.shopmini.data.model.address

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
//İl verisini karşılayan veri sınıfı
@Serializable
data class ProvinceDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)


