package com.cd.musicplayerapp.exoplayer

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.IntDef
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

interface MusicSource : Iterable<MediaMetadataCompat> {

    fun isEmpty(): Boolean

    val songs: List<MediaMetadataCompat>

    /**
     * Begins loading the data for this music source.
     */
    suspend fun load()

    /**
     * Method will perform a given action when this [Music Source] is ready to be used
     *
     * @param performAction a lamda expression to be called when the source is ready. 'true' indicates
     * the source was successfully prepared 'false' indicates an error occurred
     * */
    fun whenReady(performAction: (Boolean) -> Unit): Boolean

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource

    fun asMediaItems():  List<MediaBrowserCompat.MediaItem>

}

@IntDef(
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class State
    /**
     * State indicating the source was created, but no initialization has performed.
     */
    const val STATE_CREATED = 1

    /**
     * State indicating initialization of the source is in progress.
     */
    const val STATE_INITIALIZING = 2

    /**
     * State indicating the source has been initialized and is ready to be used.
     */
    const val STATE_INITIALIZED = 3

    /**
     * State indicating an error has occurred.
     */
    const val STATE_ERROR = 4

abstract class AbstractMusicSource: MusicSource {

    protected abstract val onReadyListeners: MutableList<(Boolean) -> Unit>

    abstract var catalog: List<MediaMetadataCompat>
    protected set

    override val songs: List<MediaMetadataCompat>
        get() = catalog

    @State
    var state: Int = STATE_CREATED
    set(value) {
        if(value == STATE_INITIALIZED  || value == STATE_ERROR) {
            synchronized(onReadyListeners) {
                field = value
                onReadyListeners.forEach { listener ->
                    listener(value == STATE_INITIALIZED)
                }
            }
        } else {
            field = value
        }
    }

    override fun isEmpty(): Boolean = catalog.isEmpty()

    /**
     * Performs an action when this MusicSource is ready.
     *
     * This method is *not* threadsafe. Ensure actions and state changes are only performed
     * on a single thread.
     */
    override fun whenReady(performAction: (Boolean) -> Unit): Boolean {
        return when(state) {
            STATE_INITIALIZING, STATE_CREATED -> {
                onReadyListeners += performAction
                false
            }
            else -> {
                performAction(state != STATE_ERROR)
                true
            }
        }
    }

    override fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        catalog.forEach { song ->
            val mediaItem = MediaItem.Builder()
                .setUri(song.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri())
                .setMediaId(song.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))
                .build()
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    override fun asMediaItems() = catalog.map { song ->
        val description = MediaDescriptionCompat.Builder()
            .setMediaUri(song.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(song.description.title)
            .setSubtitle(song.description.description)
            .setMediaId(song.description.mediaId)
            .setIconUri(song.description.iconUri)
            .build()

        MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }
}
