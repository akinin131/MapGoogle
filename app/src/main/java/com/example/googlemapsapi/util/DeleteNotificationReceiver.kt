package com.example.googlemapsapi.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DeleteNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.example.DELETE_ACTION") {
            // Ваш код для удаления уведомления
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = 1
            notificationManager.cancel(notificationId)
        }
    }
}
