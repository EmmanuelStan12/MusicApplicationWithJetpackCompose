package com.cd.musicplayerapp.ui.main

import android.support.v4.media.session.PlaybackStateCompat
import com.cd.musicplayerapp.domain.Music

data class MainState(
    val currentPlayingMusic: Music? = null,
    val musicState: MusicState = MusicState.NONE,
    val loading: Boolean = false,
    val error: String = "",
    val ready: Boolean = false,
    val musicList: List<Music> = emptyList(),
    val searchValue: String = "",
    val repeatMode: Int = repeatModeList[0],
    val currentSelectedAlbum: String = ""
) {
    val albumFilteredList: List<Music>
        get() = musicList.filter {
            it.album == currentSelectedAlbum
        }
}

enum class MusicState {
    PLAYING, PAUSED, NONE
}

val repeatModeList = listOf(
    PlaybackStateCompat.REPEAT_MODE_ALL,
    PlaybackStateCompat.REPEAT_MODE_ONE,
    PlaybackStateCompat.REPEAT_MODE_NONE,
    PlaybackStateCompat.SHUFFLE_MODE_ALL
)


