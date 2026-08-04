package com.example.shopmini.domain.usecase.auth

import com.example.shopmini.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Yeni kullanıcı kaydı işlemini başlatan Use Case.
 *
 * ViewModel bu sınıfı çağırır; bu sınıf ise [AuthRepository] üzerinden
 * Supabase'e kayıt isteği gönderir. ViewModel, Supabase'i hiç bilmez.
 *
 * operator fun invoke() kullanımı sayesinde ViewModel içinde
 * doğrudan fonksiyon gibi çağrılabilir: signUpUseCase(email, password, ...)
 */
class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String
    ): Result<Unit> {
      return  authRepository.signUp(
            email,
            password,
            firstName,
            lastName,
            phone
        )
    }
}
