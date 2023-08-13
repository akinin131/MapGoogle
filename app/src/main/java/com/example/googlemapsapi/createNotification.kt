package com.example.googlemapsapi

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

fun createNotification(context: Context): Notification {
    val notificationChannelId = "FileSendingChannel" // Используйте правильный ID канала

    // Создайте канал уведомления (для Android 8 и выше)
    val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            notificationChannelId,
            "File Sending Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        channel
    } else {
        null // Если версия Android меньше 8, то канал не требуется
    }

    // Создайте уведомление для Foreground Service
    val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
        .setContentTitle("File Sending Service")
        .setContentText("Waiting for file sending events")
    // Замените на вашу иконку

    return notificationBuilder.build()
}