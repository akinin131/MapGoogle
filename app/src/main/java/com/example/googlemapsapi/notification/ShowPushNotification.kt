
package com.example.googlemapsapi.notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.googlemapsapi.util.DeleteNotificationReceiver
import com.example.googlemapsapi.MainActivity
import com.example.googlemapsapi.R

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
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val deleteActionIntent = Intent(context, DeleteNotificationReceiver::class.java)
    deleteActionIntent.action = "com.example.DELETE_ACTION"
    val deleteActionPendingIntent = PendingIntent.getBroadcast(context,
        0,
        deleteActionIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
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

    val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        (vibrator?.vibrate(500))
    }

    notificationManager.notify(notificationId, notification)
}
