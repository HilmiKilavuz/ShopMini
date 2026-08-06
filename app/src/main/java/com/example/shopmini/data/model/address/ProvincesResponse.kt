package com.example.shopmini.data.model.address

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
//İl verisini karşılayan veri sınıfı
@Serializable
data class ProvincesResponse(
    @SerialName("data") val data: List<ProvinceDto>
)