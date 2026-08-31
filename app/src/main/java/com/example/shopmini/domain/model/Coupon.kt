/**
 * Domain Katmanı.
 * İndirim kuponunu temsil eden domain model.
 */
package com.example.shopmini.domain.model

// Geçerli bir kupon kodunu ve indirim yüzdesini tutar
data class Coupon(
    val code: String,
    val discountPercent: Int
)
