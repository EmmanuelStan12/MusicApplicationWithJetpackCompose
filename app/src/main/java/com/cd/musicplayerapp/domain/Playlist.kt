package com.cd.musicplayerapp.domain

import com.cd.musicplayerapp.data.PlaylistEntity

data class Playlist(
    val title: String
)

fun Playlist.toPlaylistEntity(): PlaylistEntity = PlaylistEntity(title)
