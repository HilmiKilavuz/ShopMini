package com.example.shopmini.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.usecase.auth.GetCurrentUserUseCase
import com.example.shopmini.domain.usecase.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
     val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        getCurrentUser()

    }

    fun onSignOutClick() {
        viewModelScope.launch {
            signOutUseCase()
            _uiState.update { it.copy(isSignOutSuccess = true) }
        }


    }

    fun getCurrentUser() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            val result = getCurrentUserUseCase()

            if (result != null) {

                _uiState.update {
                    it.copy(isLoading = false, userProfile = result, errorMessage = null)
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Kullanıcı bilgileri alınamadı.")
                }

            }
        }

    }


}