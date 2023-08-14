package com.example.googlemapsapi.tcp

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class DeleteNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Здесь можно добавить логику при удалении уведомления
        if (context != null) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1) // Замените 1 на ваш notificationId
        }
    }
}