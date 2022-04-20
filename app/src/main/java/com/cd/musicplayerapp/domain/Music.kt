package com.cd.musicplayerapp.domain

import com.cd.musicplayerapp.data.MusicEntity

data class Music(
    val _mediaId: String,
    val title: String,
    val duration: Long,
    val artists: List<String>,
    val imageUri: String,
    val musicUri: String,
    val lastPlaybackPosition: Long = 0L
)

fun Music.toMusicEntity() = MusicEntity(
    _mediaId,
    title,
    duration,
    artists,
    imageUri,
    musicUri,
    lastPlaybackPosition
)