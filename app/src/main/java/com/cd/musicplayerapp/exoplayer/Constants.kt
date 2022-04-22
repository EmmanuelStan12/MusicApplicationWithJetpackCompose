package com.cd.musicplayerapp.exoplayer

import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING
import com.cd.musicplayerapp.domain.Music
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

const val NOTIFICATION_CHANNEL_ID = "MUSIC_SERVICE_ID"
const val NOTIFICATION_ID = 1
const val sArtworkUri = "content://media/external/audio/albumart"

const val MEDIA_ROOT_ID = "MEDIA_ROOT_ID"
const val EMPTY_MEDIA_ROOT_ID = "EMPTY_MEDIA_ROOT_ID"

const val QUIT_ACTION = "quit_action"
const val MUSIC_IMAGE_URL = "https://images.unsplash.com/photo-1616356607338-fd87169ecf1a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"

const val METADATA_KEY_SIZE = "METADATA_KEY_SIZE"

inline val PlaybackStateCompat.isPrepared
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_PLAYING ||
            state == PlaybackStateCompat.STATE_PAUSED

inline val PlaybackStateCompat.isPlaying
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_PLAYING

//is either playing is it is paused
inline val PlaybackStateCompat.isPlayEnabled
    get() = actions and PlaybackStateCompat.ACTION_PLAY != 0L ||
            (actions and PlaybackStateCompat.ACTION_PLAY_PAUSE != 0L &&
                    state == PlaybackStateCompat.STATE_PAUSED)

fun MediaMetadataCompat.getMusic(): Music = run {
    Timber.d("getting description content ${getString(
        MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)} album ${getString(
        MediaMetadataCompat.METADATA_KEY_ALBUM)} size ${getString(METADATA_KEY_SIZE)}")
    Music(
        _mediaId = description.mediaId ?: "",
        title = description.title.toString(),
        duration = getLong(MediaMetadataCompat.METADATA_KEY_DURATION),
        artists = description.description.toString().split(","),
        imageUri = description.iconUri.toString(),
        musicUri = description.mediaUri.toString(),
        album = getString(MediaMetadataCompat.METADATA_KEY_ALBUM),
        size = getString(METADATA_KEY_SIZE)
    )
}

inline val PlaybackStateCompat.currentPlaybackPosition: Long
    get() = if(state == STATE_PLAYING) {
        val timeDelta = SystemClock.elapsedRealtime() - lastPositionUpdateTime
        (position + (timeDelta * playbackSpeed)).toLong()
    } else position

inline val Long.timeInMinutes: String
    get() {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return dateFormat.format(this)
    }