package com.cd.musicplayerapp.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.exoplayer.loadMusicImageUri
import com.cd.musicplayerapp.exoplayer.sArtworkUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MusicDatasource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    init {
        filterFilePath()
    }

    suspend fun loadLocalMusic(): List<Music> = withContext(Dispatchers.IO) {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.SIZE,
        )
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val cursor = context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        val musicList = cursor?.getMusic() ?: emptyList()
        musicList
    }

    private suspend fun Cursor.getMusic(): List<Music> {

        val musicList = mutableListOf<Music>()
        val columnID = getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val columnAlbumID = getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
        val columnTitle = getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val columnDuration = getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val columnArtist = getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val columnSize = getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
        val columnAlbum = getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        while (moveToNext()) {
            val id = getLong(columnID)
            val title = getString(columnTitle)
            val duration = getLong(columnDuration)
            val albumId = getLong(columnAlbumID)
            val artists = getString(columnArtist).split(" ")
            val musicUri =
                ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
            val bitmap = context.loadMusicImageUri(musicUri.toString())
            val size = getString(columnSize)
            val album = getString(columnAlbum)
            musicList.add(
                Music(
                    _mediaId = id.toString(),
                    title = title,
                    duration = duration,
                    artists = artists,
                    musicUri = musicUri.toString(),
                    bitmap = bitmap,
                    album = album,
                    size = size
                )
            )
        }
        Timber.d("Extensions ${musicList.joinToString(" ,")}")
        return musicList
    }

    private fun getAlbumArt(album_id: Long) =
        ContentUris.withAppendedId(Uri.parse(sArtworkUri), album_id)

    private fun filterFilePath(path: String = "/storage/emulated/0/") {
        val musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val all = Environment.getExternalStorageDirectory()
        Timber.d("Files", "Path: $musicDirectory, $all")

    }

}