package com.example.shopmini.ui.screens.profile

import com.example.shopmini.data.model.UserProfile

data class ProfileUiState(
    val isLoading: Boolean=false,
    val userProfile: UserProfile?=null,
    val errorMessage: String?="",
    val isSignOutSuccess: Boolean = false

)