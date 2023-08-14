package com.example.googlemapsapi.tcp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class FileLoadedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.yourapp.FILE_LOADED") {
            val message = "Файл успешно загружен"
            showPushNotification(context!!, message)
        }
    }
}