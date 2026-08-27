package com.example.shopmini.ui.util

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Firebase Cloud Messaging servis sınıfı.
 * - onNewToken: Cihazın FCM token'ı yenilendiğinde tetiklenir.
 * - onMessageReceived: Sunucudan bir push bildirimi geldiğinde tetiklenir.
 *
 * Şu an sunucu tarafı yok; bu sınıf ileride backend eklendiğinde
 * burayı genişletmek için hazır tutulmuştur.
 */
class ShopMiniMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // İleride: token'ı Supabase'deki kullanıcı profiline kaydet
        // Böylece sunucu bu cihaza push gönderebilir
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // İleride: sunucudan gelen mesajı burada işle
    }
}
