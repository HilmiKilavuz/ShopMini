package com.example.shopmini.domain.usecase.auth

import com.example.shopmini.data.model.UserProfile
import com.example.shopmini.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Şu an giriş yapmış olan kullanıcının profil bilgilerini getiren Use Case.
 *
 * Profil ekranını doldurmak veya kullanıcının adını göstermek gibi
 * durumlarda ViewModel bu sınıfı çağırarak [UserProfile] bilgisine ulaşır.
 *
 * @return Giriş yapmış kullanıcının [UserProfile] nesnesi; yoksa null.
 */
class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /** Repository'den aktif kullanıcının profil bilgisini çeker. */
    suspend operator fun invoke(): UserProfile? {
        return authRepository.getCurrentUser()
    }
}