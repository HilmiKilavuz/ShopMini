package com.example.shopmini.ui.screens.profile.editprofile

data class EditProfileUiState(
    val isLoading: Boolean = false,

    // Form alanları — başta mevcut değerler ile dolacak
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",

    // Şifre alanları
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",

    // Validation hataları — Profil formu
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val phoneError: String? = null,

    // Validation hataları — Şifre formu
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null,

    // Genel sonuç
    val profileUpdateSuccess: Boolean = false,
    val passwordUpdateSuccess: Boolean = false,
    val errorMessage: String? = null
)
