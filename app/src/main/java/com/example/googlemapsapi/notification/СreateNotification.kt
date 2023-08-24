package com.example.googlemapsapi.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

fun createNotification(context: Context): Notification {
    val notificationChannelId = "FileSendingChannel"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            notificationChannelId,
            "File Sending Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle("File Sending Service")
            .setContentText("Waiting for file sending events")
            .build()
    } else {
        return NotificationCompat.Builder(context)
            .setContentTitle("File Sending Service")
            .setContentText("Waiting for file sending events")
            .build()
    }
}

