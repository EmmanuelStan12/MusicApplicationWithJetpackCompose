package com.cd.musicplayerapp.exoplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.v4.media.session.PlaybackStateCompat
import com.cd.musicplayerapp.ui.main.MusicState

fun PlaybackStateCompat.getMusicState(): MusicState = when {
    isPlaying -> MusicState.PLAYING
    isPrepared -> MusicState.PAUSED
    else -> MusicState.NONE
}

fun Context.loadMusicImageUri(uri: Uri): Bitmap? {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(this, uri)
    return try {
        val coverImage = mediaMetadataRetriever.embeddedPicture
        BitmapFactory.decodeByteArray(coverImage, 0, coverImage!!.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
