package com.cd.musicplayerapp.exoplayer

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val TAG = "MediaPlaybackService"

@AndroidEntryPoint
class MediaPlaybackService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat

    companion object {
        var currentSongDuration = 0L
            private set
    }

    @Inject
    lateinit var musicSource: MusicSource

    private var currentPlayingSong: MediaMetadataCompat? = null

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory

    private var isPlayerInitialized = false

    private lateinit var musicNotificationManager: MusicNotificationManager

    var isInForeground = false

    private val serviceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var musicPlaybackPreparer: MusicPlaybackPreparer

    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    private lateinit var connector: MediaSessionConnector

    private lateinit var musicNavigator: MusicQueueNavigator

    override fun onCreate() {
        super.onCreate()
        initialize()
        scope.launch {
            musicSource.load()
        }
        exoPlayer.addListener(musicPlayerEventListener)
        musicNotificationManager.showNotification(exoPlayer)
    }

    private fun initialize() {
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        mediaSession = MediaSessionCompat(baseContext, TAG)
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }

        sessionToken = mediaSession.sessionToken

        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        ) {
            currentSongDuration = exoPlayer.duration
        }

        musicPlaybackPreparer = MusicPlaybackPreparer(
            musicSource
        ) {
            currentPlayingSong = it
            preparePlayer(musicSource.songs, it, true)
        }

        musicPlayerEventListener = MusicPlayerEventListener(
            showMessage = { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() },
            showNotification = {
                if(it) musicNotificationManager.showNotification(exoPlayer)
                else musicNotificationManager.hideNotification()
            },
            stopForeground = {
                stopForeground(false)
            }
        )

        musicNavigator = MusicQueueNavigator()
        connector = MediaSessionConnector(mediaSession).apply {
            setPlaybackPreparer(musicPlaybackPreparer)
            setQueueNavigator(MusicQueueNavigator())
            setPlayer(exoPlayer)
        }
    }

    private inner class MusicQueueNavigator: TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return musicSource.songs[windowIndex].description
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return if(allowBrowsing(clientPackageName, clientUid)) {
            // Returns a root ID that clients can use with onLoadChildren() to retrieve
            // the content hierarchy.
            MediaBrowserServiceCompat.BrowserRoot(MEDIA_ROOT_ID, null)
        } else {
            // Clients can connect, but this BrowserRoot is an empty hierachy
            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            MediaBrowserServiceCompat.BrowserRoot(EMPTY_MEDIA_ROOT_ID, null)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)

        /**
         * By stopping playback, the player will transition to [Player.STATE_IDLE] triggering
         * [Player.EventListener.onPlayerStateChanged] to be called. This will cause the
         * notification to be hidden and trigger
         * [PlayerNotificationManager.NotificationListener.onNotificationCancelled] to be called.
         * The service will then remove itself as a foreground service, and will call
         * [stopSelf].
         */
        exoPlayer.stop()
    }

    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }

        // Cancel coroutines when the service is going away.
        serviceJob.cancel()

        // Free ExoPlayer resources.
        exoPlayer.removeListener(musicPlayerEventListener)
        exoPlayer.release()
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat,
        playNow: Boolean
    ) {
        val currentSongIndex = if(currentPlayingSong == null) 0 else songs.indexOf(itemToPlay)
        exoPlayer.setMediaSource(musicSource.asMediaSource(dataSourceFactory))
        exoPlayer.prepare()
        exoPlayer.seekTo(currentSongIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }

    private fun allowBrowsing(clientPackageName: String, clientUid: Int): Boolean {
        return true
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val resultSent = musicSource.whenReady { isInitialized ->
            if(isInitialized) {
                result.sendResult(musicSource.asMediaItems().toMutableList())
                if(!isPlayerInitialized && !musicSource.isEmpty()) {
                    preparePlayer(
                        musicSource.songs,
                        musicSource.songs[0],
                        false
                    )
                }
            } else {
                result.sendResult(null)
            }
        }
        if(!resultSent) result.detach()
    }

    private inner class PlayerNotificationListener: PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isInForeground) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MediaPlaybackService.javaClass)
                )

                startForeground(notificationId, notification)
                isInForeground = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isInForeground = false
            stopSelf()
        }
    }
}