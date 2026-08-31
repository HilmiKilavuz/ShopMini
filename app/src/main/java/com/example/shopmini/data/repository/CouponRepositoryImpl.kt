/**
 * DATA Katmanı.
 * CouponRepository'nin Supabase PostgREST ile gerçekleştirilen implementasyonu.
 */
package com.example.shopmini.data.repository

import com.example.shopmini.data.model.CouponDto
import com.example.shopmini.domain.model.Coupon
import com.example.shopmini.domain.repository.CouponRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class CouponRepositoryImpl @Inject constructor(
    private val supaBase: SupabaseClient
) : CouponRepository {

    // Supabase'deki coupons tablosunda kodu ve is_active=true'yu filtreler
    override suspend fun validate(code: String): Coupon? {
        val result = supaBase.postgrest["coupons"]
            .select {
                filter {
                    eq("code", code)
                    eq("is_active", true)
                }
            }
            .decodeList<CouponDto>()
            .firstOrNull() ?: return null

        return Coupon(
            code = result.code,
            discountPercent = result.discount
        )
    }
}
