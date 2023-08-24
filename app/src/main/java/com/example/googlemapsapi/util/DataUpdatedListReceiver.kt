package com.example.googlemapsapi.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DataUpdatedListReceiver(private val onUpdateList: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.example.DATA_UPDATED") {
            onUpdateList.invoke()
        }
    }
}
