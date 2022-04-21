package com.cd.musicplayerapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cd.musicplayerapp.domain.Music

@Entity(tableName = "music_entity")
data class MusicEntity(
    @PrimaryKey(autoGenerate = false) val _id: String,
    val title: String,
    val duration: Long,
    val artists: List<String>,
    val imageUri: String,
    val musicUri: String,
    val lastPlaybackPosition: Long = 0L,
    val album: String = "",
    val size: String = ""
)

fun MusicEntity.toMusic() = Music(
    _id ?: "",
    title,
    duration,
    artists,
    imageUri,
    musicUri,
    lastPlaybackPosition,
    album,
    size
)

