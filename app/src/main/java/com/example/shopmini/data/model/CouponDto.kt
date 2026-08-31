/**
 * DATA Katmanı.
 * Supabase `coupons` tablosundan gelen JSON verisini karşılayan DTO.
 */
package com.example.shopmini.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Supabase'deki coupons tablosunun kolonlarına birebir karşılık gelir
@Serializable
data class CouponDto(
    @SerialName("code")
    val code: String,
    @SerialName("discount")
    val discount: Int,
    @SerialName("is_active")
    val isActive: Boolean
)
