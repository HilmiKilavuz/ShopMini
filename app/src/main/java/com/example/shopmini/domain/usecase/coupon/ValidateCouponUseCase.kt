/**
 * Domain Katmanı.
 * Kullanıcının girdiği kupon kodunu doğrulayan Use Case.
 */
package com.example.shopmini.domain.usecase.coupon

import com.example.shopmini.domain.model.Coupon
import com.example.shopmini.domain.repository.CouponRepository
import javax.inject.Inject

// Tek iş kuralı: kodu temizle, büyük harfe çevir, doğrula
class ValidateCouponUseCase @Inject constructor(
    private val couponRepository: CouponRepository
) {
    suspend operator fun invoke(code: String): Coupon? =
        couponRepository.validate(code.trim().uppercase())
}
