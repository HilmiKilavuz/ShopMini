package com.example.shopmini.ui.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.EntryPointAccessors
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Firebase Cloud Messaging servis sınıfı.
 * - onNewToken: Cihazın FCM token'ı yenilendiğinde Supabase'deki profiles tablosunu günceller.
 * - onMessageReceived: Uygulama açıkken sunucudan mesaj gelirse burası tetiklenir.
 *
 * Not: FirebaseMessagingService Android sistemi tarafından oluşturulduğu için
 * doğrudan @Inject kullanılamaz. Hilt'in EntryPoint API'si bu sorunu çözer.
 */
class ShopMiniMessagingService : FirebaseMessagingService() {

    // Hilt'ten SupabaseClient'ı almak için EntryPoint tanımı
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface FcmEntryPoint {
        fun supabaseClient(): SupabaseClient
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "Yeni token alındı ve işleniyor...")
        saveTokenToSupabase(token)
    }

    private fun saveTokenToSupabase(token: String) {
        val supabase = try {
            EntryPointAccessors
                .fromApplication(applicationContext, FcmEntryPoint::class.java)
                .supabaseClient()
        } catch (e: Exception) {
            Log.e("FCM_TOKEN", "Supabase client alınamadı: ${e.message}")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                supabase.from("profiles").update(
                    mapOf("fcm_token" to token)
                ) {
                    filter { eq("id", userId) }
                }
                Log.d("FCM_TOKEN", "Token Supabase'e kaydedildi.")
            } catch (e: Exception) {
                Log.e("FCM_TOKEN", "Token kaydedilemedi: ${e.message}")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM_MESSAGE", "Mesaj alındı: ${remoteMessage.notification?.title}")
    }
}

