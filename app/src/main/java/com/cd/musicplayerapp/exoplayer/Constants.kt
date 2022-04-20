package com.cd.musicplayerapp.exoplayer

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.cd.musicplayerapp.domain.Music

const val NOTIFICATION_CHANNEL_ID = "MUSIC_SERVICE_ID"
const val NOTIFICATION_ID = 1
const val sArtworkUri = "content://media/external/audio/albumart"

const val MEDIA_ROOT_ID = "MEDIA_ROOT_ID"
const val EMPTY_MEDIA_ROOT_ID = "EMPTY_MEDIA_ROOT_ID"

const val QUIT_ACTION = "quit_action"
const val MUSIC_IMAGE_URL = "https://images.unsplash.com/photo-1616356607338-fd87169ecf1a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"

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

fun MediaMetadataCompat.getMusic(): Music = Music(
    _mediaId = description.mediaId ?: "",
    title = description.title.toString(),
    duration = getLong(MediaMetadataCompat.METADATA_KEY_DURATION),
    artists = description.subtitle.toString().split(","),
    imageUri = description.iconUri.toString(),
    musicUri = description.mediaUri.toString()
)