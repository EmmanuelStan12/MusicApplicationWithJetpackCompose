package com.cd.musicplayerapp.data

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.exoplayer.loadMusicImageUri

@Entity(tableName = "music_entity")
data class MusicEntity(
    @PrimaryKey(autoGenerate = false) val _id: String,
    val title: String,
    val duration: Long,
    val artists: List<String>,
    val musicUri: String,
    val album: String = "",
    val size: String = ""
)

suspend fun MusicEntity.toMusic(context: Context): Music {
    val bitmap = context.loadMusicImageUri(musicUri)
    return Music(
        _mediaId = _id ?: "",
        title = title,
        duration = duration,
        artists = artists,
        musicUri = musicUri,
        album = album,
        size = size,
        bitmap = bitmap
    )
}

