package com.cd.musicplayerapp.exoplayer

import android.drm.DrmErrorEvent.TYPE_OUT_OF_MEMORY
import android.widget.Toast
import com.cd.musicplayerapp.R
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import timber.log.Timber

class MusicPlayerEventListener(
    private val showNotification: (Boolean) -> Unit,
    private val stopForeground: (Boolean) -> Unit,
    private val showMessage: (String) -> Unit,
) : Player.Listener {
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING,
            Player.STATE_READY -> {
                showNotification(true)

                // If playback is paused we remove the foreground state which allows the
                // notification to be dismissed. An alternative would be to provide a "close"
                // button in the notification which stops playback and clears the notification.
                if (playbackState == Player.STATE_READY) {
                    stopForeground(false)
                }
            }
            else -> {
                showNotification(false)
            }
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        showMessage(error.message ?: error.localizedMessage ?: "An unknown error occurred")
    }
}
