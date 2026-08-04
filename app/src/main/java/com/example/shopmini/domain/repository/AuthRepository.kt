package com.example.shopmini.domain.repository

import com.example.shopmini.data.model.UserProfile


//Auth işlemleri için bir interface
interface AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String
    ): Result<Unit>

    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signOut()
    suspend fun getCurrentUser(): UserProfile?
    fun isUserLoggedIn(): Boolean
}