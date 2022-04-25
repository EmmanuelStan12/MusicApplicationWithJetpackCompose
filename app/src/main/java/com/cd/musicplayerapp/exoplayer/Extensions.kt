package com.cd.musicplayerapp.exoplayer

import android.support.v4.media.session.PlaybackStateCompat
import com.cd.musicplayerapp.ui.main.MusicState

fun PlaybackStateCompat.getMusicState(): MusicState = when {
    isPlaying -> MusicState.PLAYING
    isPrepared -> MusicState.PAUSED
    else -> MusicState.NONE
}

fun Long.fromMillis() {

}
