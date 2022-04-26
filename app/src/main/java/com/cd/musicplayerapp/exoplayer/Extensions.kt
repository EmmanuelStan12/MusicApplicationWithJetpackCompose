package com.cd.musicplayerapp.exoplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.net.toUri
import com.cd.musicplayerapp.ui.main.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun PlaybackStateCompat.getMusicState(): MusicState = when {
    isPlaying -> MusicState.PLAYING
    isPrepared -> MusicState.PAUSED
    else -> MusicState.NONE
}

suspend fun Context.loadMusicImageUri(uri: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(this@loadMusicImageUri, uri.toUri())
        try {
            val coverImage = mediaMetadataRetriever.embeddedPicture
            coverImage?.let {
                BitmapFactory.decodeByteArray(coverImage, 0, it.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
