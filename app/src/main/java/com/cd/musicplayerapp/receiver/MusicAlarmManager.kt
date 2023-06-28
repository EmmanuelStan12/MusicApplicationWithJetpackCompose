package com.cd.musicplayerapp.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

private const val REQUEST_CODE = 0
private const val FLAGS = 0

class MusicAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmManagerPendingIntent: PendingIntent

    fun initialize() {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManagerPendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, REQUEST_CODE, intent, FLAG_IMMUTABLE)
        }

        alarmManager?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            alarmManagerPendingIntent
        )
    }
}