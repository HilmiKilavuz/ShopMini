package com.example.shopmini.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Supabase veritabanındaki 'profiles' tablosunu temsil eden veri sınıfı.
 *
 * Kullanıcının giriş/kayıt bilgileri Supabase Auth tarafından yönetilirken,
 * isim, soyisim ve telefon gibi ek profil bilgileri bu sınıf aracılığıyla
 * 'profiles' tablosunda tutulur.
 *
 * @property id         Supabase Auth'tan gelen benzersiz kullanıcı kimliği (UUID formatında).
 * @property firstName  Kullanıcının adı. Veritabanında 'first_name' sütununa karşılık gelir.
 * @property lastName   Kullanıcının soyadı. Veritabanında 'last_name' sütununa karşılık gelir.
 * @property phone      Kullanıcının telefon numarası. İsteğe bağlıdır (nullable).
 * @property email      Kullanıcının e-posta adresi.
 *
 * Not: @SerialName anotasyonu, Kotlin'in camelCase alan adlarını (örn. firstName)
 * Supabase'in snake_case sütun adlarıyla (örn. first_name) eşleştirir.
 */
@Serializable
data class UserProfile(
    val id: String,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    val phone: String?,
    val email: String,

    /** Firebase Cloud Messaging cihaz token'ı. Sunucu push bildirimleri için kullanılır. */
    @SerialName("fcm_token")
    val fcmToken: String? = null
)