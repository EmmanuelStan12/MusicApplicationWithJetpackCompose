package com.cd.musicplayerapp.domain

import android.graphics.Bitmap
import com.cd.musicplayerapp.data.MusicEntity

data class Music(
    val _mediaId: String,
    val title: String,
    val duration: Long,
    val artists: List<String>,
    val bitmap: Bitmap?,
    val musicUri: String,
    val album: String = "",
    val size: String = ""
)

fun Music.toMusicEntity() = MusicEntity(
    _id = _mediaId,
    title = title,
    duration = duration,
    artists = artists,
    musicUri = musicUri,
    album = album,
    size = size
)

val emptyMusic = Music(
    "",
    "",
    0L,
    emptyList<String>(),
    null,
    ""
)