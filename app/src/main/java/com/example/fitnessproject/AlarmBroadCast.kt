package com.example.fitnessproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fitnessproject.ui.activities.splash.SplashActivity

class AlarmBroadCast : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let { context ->
            showNotification(
                context = context,
                title = "Thông báo",
                body = "Đã đến giờ tập luyện"
            )
        }
    }

    private fun showNotification(context: Context, title: String, body: String) {
        val channelId = "Fitness"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_clock)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_clock))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(LongArray(0))
            .setContentTitle(title)
            .setContentText(body)
        val notificationManager =
            context.applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, SplashActivity::class.java)
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val contentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        builder.setContentIntent(contentIntent)

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(1, builder.build())
    }
}