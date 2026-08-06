package com.example.shopmini.data.model.address

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
//İlçe verisini karşılayan veri sınıfı
@Serializable
data class DistrictDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

