package com.example.shopmini.ui.util

/**
 * Form alanlarının geçerliliğini kontrol eden regex tabanlı doğrulama yardımcısı.
 *
 * Tüm fonksiyonlar saf (pure) fonksiyonlardır; dışarıdan bağımlılık almaz,
 * sadece verilen String'i kontrol edip true/false döndürür.
 *
 * LoginViewModel ve SignUpViewModel tarafından "Kayıt Ol / Giriş Yap" butonuna
 * basıldığında verilerin Supabase'e gönderilmeden önce kontrol edilmesi için kullanılır.
 */
object Validators {

    /**
     * E-posta adresinin geçerli formatta olup olmadığını kontrol eder.
     *
     * Geçerli format: kullanici@alan.com
     * Başındaki ve sonundaki boşluklar otomatik temizlenir (trim).
     *
     * @param email Kontrol edilecek e-posta adresi.
     * @return Geçerli formattaysa true, değilse false.
     */
    fun isValidEmail(email: String): Boolean {
        val pattern = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
        return pattern.matches(email.trim())
    }

    /**
     * Şifrenin güvenlik kurallarına uyup uymadığını kontrol eder.
     *
     * Kurallar:
     * - En az 8 karakter uzunluğunda olmalı.
     * - En az 1 büyük harf içermeli.
     * - En az 1 rakam içermeli.
     *
     * @param password Kontrol edilecek şifre.
     * @return Kurallara uygunsa true, değilse false.
     */
    fun isValidPassword(password: String): Boolean {
        val pattern = Regex("^(?=.*[A-Z])(?=.*\\d).{8,}$")
        return pattern.matches(password)
    }

    /**
     * Türkiye formatındaki telefon numarasının geçerli olup olmadığını kontrol eder.
     *
     * Desteklenen format: 5XX XXX XX XX (başında 0 veya +90 olmadan)
     * Boşluk ve tire karakterleri kontrol öncesinde otomatik temizlenir.
     *
     * @param phone Kontrol edilecek telefon numarası.
     * @return Geçerli Türkiye cep telefonu formatındaysa true, değilse false.
     */
    fun isValidPhone(phone: String): Boolean {
        val pattern = Regex("^(5)(0[5-9]|[3-9][0-9])[0-9]{7}$")
        return pattern.matches(phone.replace(" ", "").replace("-", ""))
    }

    /**
     * Ad veya soyadının geçerli formatta olup olmadığını kontrol eder.
     *
     * Kurallar:
     * - Yalnızca harf içermeli (Türkçe karakterler dahil: ğ, ü, ş, ı, ö, ç).
     * - En az 2 karakter uzunluğunda olmalı.
     * - Başındaki ve sonundaki boşluklar otomatik temizlenir (trim).
     *
     * @param name Kontrol edilecek ad veya soyad.
     * @return Geçerli formattaysa true, değilse false.
     */
    fun isValidName(name: String): Boolean {
        val pattern = Regex("^[a-zA-ZğüşıöçĞÜŞİÖÇ]{2,}$")
        return pattern.matches(name.trim())
    }
}