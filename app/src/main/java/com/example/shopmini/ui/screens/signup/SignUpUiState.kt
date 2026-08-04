package com.example.shopmini.ui.screens.signup

/**
 * Kayıt (SignUp) ekranının anlık görüntü durumunu (UI State) tutan veri sınıfı.
 *
 * ViewModel bu sınıfı güncelleyerek formdaki hata mesajlarını,
 * yükleme animasyonunu ve kayıt başarı durumunu ekrana yansıtır.
 *
 * @property isLoading             Kayıt isteği sürerken true; buton devre dışı kalır.
 * @property errorMessage          Supabase'den dönen genel hata mesajı.
 * @property isSignUpSuccess       Kayıt başarıyla tamamlandıysa true; yönlendirme tetiklenir.
 * @property firstNameError        Ad alanına özgü doğrulama hata mesajı.
 * @property lastNameError         Soyad alanına özgü doğrulama hata mesajı.
 * @property emailError            E-posta alanına özgü format hata mesajı.
 * @property phoneError            Telefon alanına özgü regex hata mesajı.
 * @property passwordError         Şifre karmaşıklığı kuralı hata mesajı.
 * @property confirmPasswordError  Şifre tekrar uyuşmazlığı hata mesajı.
 */
data class SignUpUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpSuccess: Boolean = false,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isClarificationAccepted: Boolean=false,
    val isKvkkAccepted: Boolean=false,
    val showTermsError: Boolean= false

)
