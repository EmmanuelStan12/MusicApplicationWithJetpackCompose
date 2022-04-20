package com.cd.musicplayerapp.domain

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.cd.musicplayerapp.exoplayer.*
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ViewModelScoped
class MusicUseCase @Inject constructor(private val musicConnection: MusicServiceConnection) {

    val currentSong = musicConnection.currentSong
    val playbackState = musicConnection.playbackState

    val timePassed = flow {
        while (true) {
            val duration = playbackState.value?.position ?: 0
            emit(duration)
            delay(1000L)
        }
    }

    suspend fun subscribeToService(): Resource<List<MediaBrowserCompat.MediaItem>> =
        suspendCoroutine { continuation ->
            musicConnection.subscribe(
                MEDIA_ROOT_ID,
                object : MediaBrowserCompat.SubscriptionCallback() {
                    override fun onChildrenLoaded(
                        parentId: String,
                        children: MutableList<MediaBrowserCompat.MediaItem>
                    ) {
                        super.onChildrenLoaded(parentId, children)
                        Timber.d("children loaded ${children.joinToString(" ,")}")
                        continuation.resume(Resource.Success(children))
                    }

                    override fun onError(parentId: String) {
                        super.onError(parentId)
                        continuation.resume(Resource.Error("Failed to connect to service"))
                    }
                }
            )
        }

    fun unsubscribeToService() = musicConnection.unsubscribe(MEDIA_ROOT_ID)

    fun skipToNextTrack() = musicConnection.skipToNextTrack()

    fun skipToPrevTrack() = musicConnection.skipToPrev()

    fun seekTo(pos: Long) = musicConnection.seekTo(pos)

    fun fastForward() = musicConnection.fastForward()

    fun rewind() = musicConnection.rewind()

    fun stopPlaying() = musicConnection.stopPlaying()

    fun playFromMediaId(mediaId: String) = musicConnection.playFromMediaId(mediaId)

    fun playPause(musicId: String, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && musicId == currentSong.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))
            playPauseCurrentSong(toggle)
        else playFromMediaId(musicId)
    }

    private fun playPauseCurrentSong(toggle: Boolean) {
        playbackState.value?.let {
            when {
                it.isPlaying -> if (toggle) musicConnection.pause()
                it.isPlayEnabled -> musicConnection.play()
                else -> Unit
            }
        }
    }
}