package com.example.shopmini.domain.usecase.auth

import com.example.shopmini.domain.repository.AuthRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(newPassword: String): Result<Unit> =
        repository.updatePassword(newPassword)
}