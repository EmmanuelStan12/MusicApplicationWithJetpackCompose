package com.cd.musicplayerapp.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.cd.musicplayerapp.domain.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicServiceConnection @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val _connectionEvent = MutableSharedFlow<Resource<Boolean>>()
    val connectionEvent = _connectionEvent.asSharedFlow()

    private val _currentSong = MutableStateFlow<MediaMetadataCompat?>(null)
    val currentSong = _currentSong.asStateFlow()

    private val connectionCallback = ConnectionCallback()

    private lateinit var mediaControllerCompat: MediaControllerCompat

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //try to connect to the music service which in turn calls
    //the onLoadChildren Function
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MediaPlaybackService::class.java),
        connectionCallback,
        null
    ).apply {
        connect()
    }

    private val transportControls: MediaControllerCompat.TransportControls
        get() = mediaControllerCompat.transportControls

    private val _playbackState = MutableStateFlow<PlaybackStateCompat?>(null)
    val playbackState = _playbackState.asStateFlow()

    fun subscribe(parentId: String, callbacks: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callbacks)
    }

    fun unsubscribe(parentId: String) {
        mediaBrowser.unsubscribe(parentId)
    }

    fun stopPlaying() = transportControls.stop()

    fun seekTo(position: Long) = transportControls.seekTo(position)

    fun pause() = transportControls.pause()

    fun play() = transportControls.play()

    fun fastForward() = transportControls.fastForward()

    fun rewind() = transportControls.rewind()

    fun skipToNextTrack()= transportControls.skipToNext()

    fun skipToPrev() = transportControls.skipToPrevious()

    fun playFromMediaId(mediaId: String) = transportControls.playFromMediaId(mediaId, null)

    private inner class ConnectionCallback: MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()
            mediaControllerCompat = MediaControllerCompat(context, mediaBrowser.sessionToken)
                .apply {
                    registerCallback(MediaControllerCallback())
                }
            coroutineScope.launch { _connectionEvent.emit(Resource.Success(true)) }
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            coroutineScope.launch { _connectionEvent.emit(Resource.Error("connection suspended")) }
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            coroutineScope.launch { _connectionEvent.emit(Resource.Error("connection failed")) }
        }
    }

    private inner class MediaControllerCallback(): MediaControllerCompat.Callback() {
        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            connectionCallback.onConnectionSuspended()
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            coroutineScope.launch {
                _connectionEvent.emit(Resource.Error(event ?: "an unknown error occured"))
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            coroutineScope.launch {
                _playbackState.emit(state)
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            coroutineScope.launch { _currentSong.emit(metadata) }
        }
    }
}