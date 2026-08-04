package com.example.shopmini.ui.screens.login

/**
 * Login ekranının anlık görüntü durumunu (UI State) tutan veri sınıfı.
 *
 * ViewModel bu sınıfı güncelleyerek ekranı yeniden çizer.
 * Tüm alanlar varsayılan değerlerle başlar; değişen sadece güncellenir.
 *
 * @property isLoading         Giriş işlemi devam ediyorsa true; buton devre dışı kalır.
 * @property errorMessage      Supabase'den gelen genel hata mesajı (yanlış şifre vb.)
 * @property isLoginSuccess    Giriş başarıldıysa true; ekran geçişi tetiklenir.
 * @property emailError        E-posta alanına özgü regex/format hata mesajı.
 * @property passwordError     Şifre alanına özgü hata mesajı.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false,
    // Form field hata mesajları
    val emailError: String? = null,
    val passwordError: String? = null
)