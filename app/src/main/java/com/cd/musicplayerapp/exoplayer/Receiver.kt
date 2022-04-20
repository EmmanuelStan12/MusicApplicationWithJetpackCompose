package com.cd.musicplayerapp.exoplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

class Receiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            QUIT_ACTION -> {
                Timber.d("Logging from broadcast receiver")
            }
        }
    }
}