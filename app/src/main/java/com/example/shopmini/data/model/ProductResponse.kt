/**
 * DATA Katmanı.
 * API'den dönen sayfalama  gibi meta verileri barındıran cevap modelidir.
 */
package com.example.shopmini.data.model

import kotlinx.serialization.Serializable
//ProductResponse verisini karşılayan veri sınıfı
@Serializable
data class ProductResponse(
    val products: List<Product>
)
