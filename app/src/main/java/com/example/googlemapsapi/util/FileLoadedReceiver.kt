package com.example.googlemapsapi.util

import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import com.example.googlemapsapi.notification.showPushNotification

class FileLoadedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.yourapp.FILE_LOADED") {
            val message = "Файл успешно загружен"

            showPushNotification(context!!, message)
        }
    }
}