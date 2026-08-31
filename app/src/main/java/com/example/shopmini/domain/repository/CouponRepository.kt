/**
 * Domain Katmanı.
 * Kupon doğrulama işlemleri için soyut repository arayüzü.
 */
package com.example.shopmini.domain.repository

import com.example.shopmini.domain.model.Coupon

// Kupon doğrulama sözleşmesi — implementasyondan bağımsız
interface CouponRepository {
    // Verilen kodu Supabase'de arar; geçerliyse Coupon, değilse null döner
    suspend fun validate(code: String): Coupon?
}
