package com.example.googlemapsapi.tcp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.example.googlemapsapi.MainActivity
import com.example.googlemapsapi.R

@SuppressLint("UnspecifiedImmutableFlag")
fun showPushNotification(context: Context, message: String) {
    val notificationId = 1
    val channelId = "FileSendingChannel"

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "File Sending Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    // Intent для действия удаления
    val deleteActionIntent = Intent(context, DeleteNotificationReceiver::class.java)
    deleteActionIntent.action = "com.yourapp.DELETE_ACTION"
    val deleteActionPendingIntent = PendingIntent.getBroadcast(context, 0, deleteActionIntent, 0)
    val deleteAction = NotificationCompat.Action.Builder(
        android.R.drawable.ic_menu_delete, // Иконка действия
        "Delete", // Текст действия
        deleteActionPendingIntent
    ).build()

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("File Uploaded")
        .setContentText(message)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(deleteAction) // Добавление кнопки удаления
        .build()

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(500)
    }

    notificationManager.notify(notificationId, notification)
}
