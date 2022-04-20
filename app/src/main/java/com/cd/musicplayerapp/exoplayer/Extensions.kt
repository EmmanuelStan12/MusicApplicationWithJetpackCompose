package com.cd.musicplayerapp.exoplayer

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.ui.home.MusicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun Context.getLocalMusicList() = withContext(Dispatchers.IO) {
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM_ID
    )
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

    val cursor = contentResolver.query(
        collection,
        projection,
        null,
        null,
        sortOrder
    )
    val musicList = cursor?.getMusic() ?: emptyList()
    musicList
}

private fun Cursor.getMusic(): List<Music> {

    val musicList = mutableListOf<Music>()
    val idColumn = getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val albumIdColumn = getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
    val titleColumn = getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
    val durationColumn = getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
    val artistsColumn = getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
    while (moveToNext()) {
        val id = getLong(idColumn)
        val title = getString(titleColumn)
        val duration = getLong(durationColumn)
        val albumId = getLong(albumIdColumn)
        val artists = getString(artistsColumn).split(" ")
        val musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        val imageUri = getAlbumArt(albumId).toString()
        musicList += Music(
            id.toString(),
            title,
            duration,
            artists,
            imageUri,
            musicUri.toString()
        )
    }
    Timber.d("Extensions ${musicList.joinToString(" ,")}")
    return musicList
}

private fun getAlbumArt(album_id: Long) =
    ContentUris.withAppendedId(Uri.parse(sArtworkUri), album_id)

fun PlaybackStateCompat.getMusicState(): MusicState = when {
    isPlaying -> MusicState.PLAYING
    isPrepared -> MusicState.PAUSED
    else -> MusicState.NONE
}

fun Long.fromMillis() {

}
