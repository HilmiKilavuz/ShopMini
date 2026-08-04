package com.example.shopmini.domain.usecase.auth

import com.example.shopmini.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Kullanıcının oturumunu kapatan Use Case.
 *
 * Profil ekranından "Çıkış Yap" butonuna basıldığında ViewModel bu sınıfı çağırır.
 * Cihazda saklanan oturum token'ı silinerek kullanıcı çıkış yapmış sayılır.
 */
class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.signOut()
    }

}