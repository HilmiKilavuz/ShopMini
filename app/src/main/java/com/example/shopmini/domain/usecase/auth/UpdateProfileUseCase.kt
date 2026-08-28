package com.example.shopmini.domain.usecase.auth

import com.example.shopmini.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String
    ): Result<Unit> = repository.updateProfile(firstName, lastName, phone)
}