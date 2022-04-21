package com.cd.musicplayerapp.domain

import com.cd.musicplayerapp.data.MusicEntity

data class Music(
    val _mediaId: String,
    val title: String,
    val duration: Long,
    val artists: List<String>,
    val imageUri: String,
    val musicUri: String,
    val lastPlaybackPosition: Long = 0L,
    val album: String = "",
    val size: String = ""
)

fun Music.toMusicEntity() = MusicEntity(
    _mediaId,
    title,
    duration,
    artists,
    imageUri,
    musicUri,
    lastPlaybackPosition,
    album,
    size
)

val emptyMusic = Music(
    "",
    "",
    0L,
    emptyList<String>(),
    "",
    ""
)