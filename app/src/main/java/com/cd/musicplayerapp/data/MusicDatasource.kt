package com.cd.musicplayerapp.data

import android.R.attr.path
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.exoplayer.sArtworkUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileFilter
import javax.inject.Inject


class MusicDatasource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    init {
        Timber.d("root dir ${context.filesDir} absolute dir ${context.filesDir.absolutePath}")
        Timber.d("external dirs ${context.getExternalFilesDir("/")}")
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

    private fun Cursor.getMusic(): List<Music> {

        val musicList = mutableListOf<Music>()
        val columnID = getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val columnAlbumID = getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
        val columnTitle = getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val columnDuration = getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val columnArtist = getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val columnDisplayName = getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
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
            val imageUri = getAlbumArt(albumId).toString()
            val displayName = getString(columnDisplayName)
            val size = getString(columnSize)
            val album = getString(columnAlbum)
            Timber.d("music with id = $id has artists $artists and $imageUri and $displayName and $size and $album")
            musicList.add(
                Music(
                    id.toString(),
                    title,
                    duration,
                    artists,
                    imageUri,
                    musicUri.toString(),
                    0L,
                    album,
                    size
                )
            )
        }
        Timber.d("Extensions ${musicList.joinToString(" ,")}")
        return musicList
    }

    private fun getAlbumArt(album_id: Long) =
        ContentUris.withAppendedId(Uri.parse(sArtworkUri), album_id)

    private fun filterFilePath(path: String = "/storage/emulated/0/") {
        val dir = File(path)
        Timber.d("listing files in directory ${dir.listFiles()?.joinToString(" ,")}")
    }

}