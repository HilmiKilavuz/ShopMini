package com.example.shopmini.domain.usecase.auth

import com.example.shopmini.domain.repository.AuthRepository
import jakarta.inject.Inject

/**
 * Mevcut kullanıcının giriş yapma işlemini başlatan Use Case.
 *
 * ViewModel bu sınıfı çağırır; bu sınıf ise [AuthRepository] üzerinden
 * Supabase Auth servisine e-posta/şifre ile giriş isteği gönderir.
 *
 * operator fun invoke() kullanımı sayesinde ViewModel içinde
 * doğrudan fonksiyon gibi çağrılabilir: signInUseCase(email, password)
 */
class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Result<Unit> {
       return authRepository.signIn(email = email, password = password)
    }
}