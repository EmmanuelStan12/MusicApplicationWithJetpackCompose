package com.cd.musicplayerapp.ui.home

import com.cd.musicplayerapp.domain.Music

data class HomeScreenState(
    val data: List<Music> = emptyList(),
    val currentPlayingMusic: Music? = null,
    val searchString: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val musicState: MusicState = MusicState.NONE,
    val timePassed: Long = 0L
)

enum class MusicState {
    PLAYING, PAUSED, NONE
}