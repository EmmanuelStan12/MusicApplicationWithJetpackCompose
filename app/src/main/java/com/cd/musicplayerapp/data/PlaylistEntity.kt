package com.cd.musicplayerapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cd.musicplayerapp.domain.Playlist

@Entity
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = false)
    val title: String,
)

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(title)
}
