package com.example.shopmini.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.usecase.auth.SignUpUseCase
import com.example.shopmini.ui.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val email = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    fun onFirstNameChanged(value: String) {
        firstName.value = value
        _uiState.update { it.copy(firstNameError = null, errorMessage = null) }
    }

    fun onLastNameChanged(value: String) {
        lastName.value = value
        _uiState.update { it.copy(lastNameError = null, errorMessage = null) }
    }

    fun onEmailChanged(value: String) {
        email.value = value
        _uiState.update { it.copy(emailError = null, errorMessage = null) }
    }

    fun onPhoneChanged(value: String) {
        phone.value = value
        _uiState.update { it.copy(phoneError = null, errorMessage = null) }
    }

    fun onPasswordChanged(value: String) {
        password.value = value
        _uiState.update { it.copy(passwordError = null, errorMessage = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        confirmPassword.value = value
        _uiState.update { it.copy(confirmPasswordError = null, errorMessage = null) }
    }

    fun onSignUpClick() {
        val fName = firstName.value
        val lName = lastName.value
        val mail = email.value
        val ph = phone.value
        val pass = password.value
        val cPass = confirmPassword.value

        val isFirstNameValid = Validators.isValidName(fName)
        val isLastNameValid = Validators.isValidName(lName)
        val isEmailValid = Validators.isValidEmail(mail)
        val isPhoneValid = ph.isBlank() || Validators.isValidPhone(ph)
        val isPasswordValid = Validators.isValidPassword(pass)
        val isConfirmMatch = pass == cPass
        if (!isFirstNameValid || !isLastNameValid || !isEmailValid || !isPhoneValid || !isPasswordValid || !isConfirmMatch) {
            _uiState.update {
                it.copy(
                    firstNameError = if (!isFirstNameValid) "Geçerli bir ad giriniz (en az 2 harf)" else null,
                    lastNameError = if (!isLastNameValid) "Geçerli bir soyad giriniz (en az 2 harf)" else null,
                    emailError = if (!isEmailValid) "Geçerli bir e-posta adresi giriniz" else null,
                    phoneError = if (!isPhoneValid) "Geçerli bir telefon giriniz (5XX...)" else null,
                    passwordError = if (!isPasswordValid) "Şifre en az 8 karakter, 1 büyük harf ve 1 rakam içermelidir" else null,
                    confirmPasswordError = if (!isConfirmMatch) "Şifreler uyuşmuyor" else null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = signUpUseCase(
                email = mail,
                password = pass,
                firstName = fName,
                lastName = lName,
                phone = ph
            )
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSignUpSuccess = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Kayıt yapılırken bir hata oluştu"
                    )
                }
            }

        }
    }

}