package com.example.shopmini.ui.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val ORDER_CHANNEL_ID = "order_notifications"
        const val ORDER_CHANNEL_NAME = "Sipariş Bildirimleri"
    }
    @SuppressLint("ServiceCast")
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    init {
        createNotificationChannel()
    }
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            ORDER_CHANNEL_ID,
            ORDER_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Sipariş durumu bildirimleri"
        }
        notificationManager.createNotificationChannel(channel)
    }
    fun sendOrderConfirmationNotification(totalAmount: Double) {
        val notification = NotificationCompat.Builder(context, ORDER_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("✅ Siparişiniz Alındı!")
            .setContentText("₺%.2f tutarındaki siparişiniz başarıyla oluşturuldu.".format(totalAmount))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1001, notification)
    }

    /** Firebase'den ön plandayken gelen mesajları ekranda göstermek için genel metod */
    fun showPushNotification(title: String, body: String) {
        val notification = NotificationCompat.Builder(context, ORDER_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        // Her bildirimin ekranda ayrı ayrı kalabilmesi için rastgele ID (şu anki zaman) veriyoruz
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
