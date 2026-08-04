package com.example.shopmini.domain.usecase.auth

import com.example.shopmini.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Kullanıcının giriş yapıp yapmadığını sorgulayan Use Case.
 *
 * MainActivity içinde uygulama açılışında hangi ekranın gösterileceğine
 * (Login mi, Ana Sayfa mı) karar vermek için kullanılır.
 * Aynı zamanda Sepet ve Profil ekranlarına erişim kontrolünde de kullanılır.
 *
 * NOT: Bu Use Case suspend değildir çünkü Supabase'e ağ isteği atmaz;
 *      sadece cihazda saklanan oturum token'ını kontrol eder (anlık sonuç).
 *
 * @return Kullanıcı giriş yapmışsa true, yapmamışsa false.
 */
class IsUserLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /** Cihazda geçerli bir oturum var mı diye kontrol eder. */
    operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}