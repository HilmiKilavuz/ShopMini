package com.example.shopmini.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.usecase.auth.SignInUseCase
import com.example.shopmini.ui.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _uiState.update {
            it.copy(emailError = null, errorMessage = null)
        }
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        _uiState.update {
            it.copy(passwordError = null, errorMessage = null)
        }

    }

    fun onLoginClick() {
        val currentEmail = _email.value
        val currentPassword = _password.value
        val isEmailValid = Validators.isValidEmail(currentEmail)
        val isPasswordValid = currentPassword.isNotBlank()
        if (!isEmailValid || !isPasswordValid) {
            _uiState.update {
                it.copy(
                    emailError = if (!isEmailValid) "Geçerli bir e-posta adresi giriniz" else null,
                    passwordError = if (!isPasswordValid) "Şifre boş olamaz" else null
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )

            }
            val result = signInUseCase(email = currentEmail, password = currentPassword)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }

            }.onFailure { error ->
                val errorMsg = error.message ?: ""
                val displayMsg = if (errorMsg.contains("invalid_credentials", ignoreCase = true) || errorMsg.contains("Invalid login credentials", ignoreCase = true)) {
                    "E-posta veya şifre hatalı."
                } else {
                    "Giriş yapılırken bir hata oluştu."
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = displayMsg
                    )
                }
            }
        }
    }


}