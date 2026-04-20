package org.hubcitydev.nor4x4.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import org.hubcitydev.nor4x4.MainActivity
import org.hubcitydev.nor4x4.R

class TimerService : Service() {

    private val CHANNEL_ID = "TimerServiceChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Nor4x4 Timer")
            .setContentText("Workout in progress")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        val ongoingActivityStatus = androidx.wear.ongoing.Status.Builder()
            .addTemplate("Timer Active")
            .build()

        val ongoingActivity = androidx.wear.ongoing.OngoingActivity.Builder(applicationContext, 1, builder)
            .setStatus(ongoingActivityStatus)
            .build()

        ongoingActivity.apply(applicationContext)

        startForeground(1, builder.build())

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Timer Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
