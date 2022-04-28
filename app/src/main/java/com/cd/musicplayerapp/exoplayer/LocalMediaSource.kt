package com.cd.musicplayerapp.exoplayer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.cd.musicplayerapp.data.MusicDao
import com.cd.musicplayerapp.data.MusicDatasource
import com.cd.musicplayerapp.domain.toMusicEntity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class LocalMediaSource @Inject constructor(
    private val source: MusicDatasource,
    private val dao: MusicDao
): AbstractMusicSource() {

    override val onReadyListeners: MutableList<(Boolean) -> Unit>
        = mutableListOf()

    override var catalog: List<MediaMetadataCompat> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

    override suspend fun load() {
        catalog = updateCatalog()
        state = STATE_INITIALIZED

    }

    override fun filter(id: String) {
        parentId = id
    }

    private suspend fun updateCatalog(): List<MediaMetadataCompat> {
        return withContext(Dispatchers.IO) {
            val songs = source.loadLocalMusic()
            dao.cacheSongs(songs.map { it.toMusicEntity() })
            songs.map { music ->
                val ( _mediaId, title, duration, artists, bitmap, musicUri, album, size ) = music

                MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artists.joinToString(" "))
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, artists.joinToString(" "))
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, _mediaId)
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, musicUri)
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                    .putString(METADATA_KEY_SIZE, size)
                    .build()
            }
        }
    }

}