package com.cd.musicplayerapp.receiver

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.cd.musicplayerapp.MainActivity
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.data.MusicDatasource
import com.cd.musicplayerapp.exoplayer.NOTIFICATION_CHANNEL_ID
import com.cd.musicplayerapp.exoplayer.NOTIFICATION_CHANNEL_NAME
import com.cd.musicplayerapp.exoplayer.SERVICE_NOTIFICATION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


class AlarmService: Service() {

    private val scope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var musicDatasource: MusicDatasource

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        updateForegroundNotification()
        startDBTask()
        return START_NOT_STICKY
    }

    private fun startDBTask() {
        scope.launch {
            val songs = musicDatasource.loadLocalMusic()
            val random = Random.nextInt(songs.size - 1)
            val song = songs[random]
            updateForegroundNotification(content = "${song.title} - ${song.artists.joinToString(" ")}", title = "Guess what we found")
        }
    }

    private fun updateForegroundNotification(content: String? = null, title: String? = null) {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        //then build the notification
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title ?: getText(R.string.notification_title))
                .setContentText(content ?: getText(R.string.notification_message))
                .setColorized(true)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentIntent(pendingIntent)
                .build().apply {
                    color = resources.getColor(R.color.dark)
            }

        startForeground(SERVICE_NOTIFICATION_ID, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(channel)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}