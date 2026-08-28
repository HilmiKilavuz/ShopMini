package com.example.shopmini.ui.screens.profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.usecase.auth.GetCurrentUserUseCase
import com.example.shopmini.domain.usecase.auth.UpdatePasswordUseCase
import com.example.shopmini.domain.usecase.auth.UpdateProfileUseCase
import com.example.shopmini.ui.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    phone = user.phone ?: ""
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }



    fun onFirstNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            firstName = value,        // yeni yazılan değeri kaydet
            firstNameError = null     // eski hata mesajını temizle
        )
    }

     fun onLastNameChanged(value: String){
        _uiState.value = _uiState.value.copy(
            lastName = value,
            lastNameError = null
        )

    }

     fun onPhoneChanged(value: String){
        _uiState.value = _uiState.value.copy(
            phone = value,
            phoneError =  null
        )

    }

     fun onNewPasswordChanged(value: String){
        _uiState.value = _uiState.value.copy(
            newPassword = value,
            newPasswordError = null
        )

    }
     fun onConfirmNewPasswordChanged(value: String){
        _uiState.value = _uiState.value.copy(
            confirmNewPassword = value,
            confirmNewPasswordError = null
        )

    }

    fun onSaveProfileClick() {

        val firstName = _uiState.value.firstName
        val lastName = _uiState.value.lastName
        val phone = _uiState.value.phone


        val isFirstNameValid = Validators.isValidName(firstName)
        val isLastNameValid  = Validators.isValidName(lastName)
        val isPhoneValid     = phone.isBlank() || Validators.isValidPhone(phone)

        if (!isFirstNameValid || !isLastNameValid || !isPhoneValid) {
            // Hata varsa state'e yaz ve DUR
            _uiState.value = _uiState.value.copy(
                firstNameError = if (!isFirstNameValid) "Geçerli bir ad giriniz (en az 2 harf)" else null,
                lastNameError  = if (!isLastNameValid)  "Geçerli bir soyad giriniz (en az 2 harf)" else null,
                phoneError     = if (!isPhoneValid)     "Geçerli bir telefon giriniz (5XX...)" else null
            )
            return  // ← Burası çok önemli. UseCase çağrılmaz.
        }


        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = updateProfileUseCase(firstName, lastName, phone)

            result
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profileUpdateSuccess = true   // Screen bunu dinleyip Snackbar gösterir
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Profil güncellenemedi"
                    )
                }
        }
    }


    fun onChangePasswordClick() {
        val newPassword     = _uiState.value.newPassword
        val confirmPassword = _uiState.value.confirmNewPassword

        val isPasswordValid = Validators.isValidPassword(newPassword)
        val isConfirmMatch  = newPassword == confirmPassword

        if (!isPasswordValid || !isConfirmMatch) {
            _uiState.value = _uiState.value.copy(
                newPasswordError     = if (!isPasswordValid) "Min 8 karakter, 1 büyük harf, 1 rakam" else null,
                confirmNewPasswordError = if (!isConfirmMatch) "Şifreler uyuşmuyor" else null
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = updatePasswordUseCase(newPassword)

            result
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        passwordUpdateSuccess = true,
                        newPassword = "",           // Başarı sonrası şifre alanlarını temizle
                        confirmNewPassword = ""
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Şifre güncellenemedi"
                    )
                }
        }
    }


}