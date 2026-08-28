package com.example.shopmini.data.repository

import com.example.shopmini.data.model.UserProfile
import com.example.shopmini.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * [AuthRepository] arayüzünün Supabase tabanlı gerçek implementasyonu.
 *
 * Tüm kimlik doğrulama işlemleri (kayıt, giriş, çıkış) ve kullanıcı profil
 * yönetimi bu sınıf üzerinden Supabase servislerine iletilir.
 *
 * Mimaride bu sınıf, Domain katmanının hiç bilmediği bir implementasyon detayıdır.
 * UseCase'ler sadece [AuthRepository] arayüzünü tanır; Supabase'i değil.
 *
 * @param supaBase Hilt tarafından enjekte edilen Supabase bağlantı istemcisi.
 */
class AuthRepositoryImpl @Inject constructor(
    private val supaBase: SupabaseClient
) : AuthRepository {

    /**
     * Yeni bir kullanıcı hesabı oluşturur ve profil bilgilerini veritabanına kaydeder.
     *
     * İki adımlı bir işlemdir:
     * 1. Supabase Auth servisine e-posta ve şifre ile kayıt isteği gönderilir.
     * 2. Oluşan kullanıcının ID'si alınarak isim, soyisim, telefon bilgileri
     *    'profiles' tablosuna eklenir.
     *
     * [runCatching] bloğu sayesinde herhangi bir hata oluşursa uygulama çökmez;
     * hata [Result.failure] olarak döndürülür.
     *
     * @return İşlem başarılıysa [Result.success], hata oluşursa [Result.failure].
     */
    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String
    ): Result<Unit> = runCatching {
        // 1. Supabase Auth servisine kayıt isteği gönder
        supaBase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        // 2. Kayıt olan kullanıcının oturum bilgilerinden ID'yi al
        //    currentSessionOrNull() önce denenir; yoksa currentUserOrNull() denenir.
        val userId = supaBase.auth.currentSessionOrNull()?.user?.id
            ?: supaBase.auth.currentUserOrNull()?.id
            ?: throw Exception("Kullanıcı oluşturuldu ancak ID bulunamadı")

        // 3. Profil nesnesini oluştur
        val profile = UserProfile(
            id = userId,
            firstName = firstName,
            lastName = lastName,
            phone = phone.ifBlank { null }, // Telefon boşsa null olarak kaydedilir
            email = email
        )

        // 4. Profil bilgilerini 'profiles' tablosuna ekle
        supaBase.from("profiles").insert(profile)
    }

    /**
     * Mevcut bir kullanıcıyı e-posta ve şifresiyle uygulamaya giriş yaptırır.
     *
     * Başarılı girişte Supabase, cihazda bir oturum (session/token) oluşturur.
     * Bu token sonraki API çağrılarında otomatik olarak kullanılır.
     *
     * @return İşlem başarılıysa [Result.success], hata oluşursa [Result.failure].
     */
    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit> = runCatching {
        // 1. Giriş yap
        supaBase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        // 2. Giriş başarılı → mevcut FCM token'ı al ve Supabase'e kaydet
        //    onNewToken sadece token değişince tetiklenir; bu satır her girişte çalışır.
        val userId = supaBase.auth.currentUserOrNull()?.id
        if (userId != null) {
            try {
                val token = com.google.firebase.messaging.FirebaseMessaging
                    .getInstance()
                    .token
                    .await()                                    // Kotlin coroutine uzantısı
                supaBase.from("profiles").update(
                    mapOf("fcm_token" to token)
                ) {
                    filter { eq("id", userId) }
                }
                android.util.Log.d("FCM_TOKEN", "Giriş sonrası token kaydedildi.")
            } catch (e: Exception) {
                // Token kaydedilemese de giriş başarılı sayılır
                android.util.Log.e("FCM_TOKEN", "Token kaydedilemedi: ${e.message}")
            }
        }
    }

    /**
     * Aktif kullanıcının oturumunu kapatır.
     *
     * Cihazda saklanan oturum token'ı silinir. Bundan sonra
     * [isUserLoggedIn] false döndürmeye başlar.
     */
    override suspend fun signOut() {
        supaBase.auth.signOut()
    }

    /**
     * Şu an giriş yapmış olan kullanıcının profil bilgilerini getirir.
     *
     * 1. Önce aktif oturumdaki kullanıcının ID'si alınır.
     * 2. Bu ID ile 'profiles' tablosuna sorgu atılır.
     * 3. Sonuç [UserProfile] nesnesine dönüştürülür.
     *
     * @return Giriş yapmış kullanıcının [UserProfile] nesnesi,
     *         yoksa veya hata oluşursa null.
     */
    override suspend fun getCurrentUser(): UserProfile? {
        // Aktif oturumda kullanıcı yoksa direkt null döndür
        val user = supaBase.auth.currentUserOrNull() ?: return null
        return try {
            supaBase.from("profiles")
                .select {
                    filter {
                        eq("id", user.id) // Sadece bu kullanıcıya ait satırı getir
                    }
                }.decodeSingleOrNull<UserProfile>()
        } catch (e: Exception) {
            null // Hata oluşursa null döndür; uygulama çökmez
        }
    }

    /**
     * Kullanıcının daha önce giriş yapıp yapmadığını kontrol eder.
     *
     * Uygulama açılışında hangi ekranın gösterileceğine karar vermek için kullanılır.
     * Cihazda geçerli bir oturum (session) varsa true, yoksa false döndürür.
     *
     * @return Kullanıcı giriş yapmışsa true, yapmamışsa false.
     */
    override fun isUserLoggedIn(): Boolean {
        return supaBase.auth.currentSessionOrNull() != null
    }

    override suspend fun updateProfile(
        firstName: String,
        lastName: String,
        phone: String
    ): Result<Unit> = runCatching {
        val userId = supaBase.auth.currentUserOrNull()?.id
            ?: throw Exception("Kullanıcı bulunamadı")
        supaBase.from("profiles").update(
            mapOf(
                "first_name" to firstName,
                "last_name"  to lastName,
                "phone"      to phone.ifBlank { null }
            )
        ) {
            filter { eq("id", userId) }
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> = runCatching {
        supaBase.auth.updateUser {
            password = newPassword
        }
    }

}