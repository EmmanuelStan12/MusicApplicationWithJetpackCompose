package com.cd.musicplayerapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Intent(context, AlarmService::class.java).also {
            context?.startService(it)
        }
    }
}