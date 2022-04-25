package com.cd.musicplayerapp.ui.main

import com.cd.musicplayerapp.domain.Music

data class MainState(
    val currentPlayingMusic: Music? = null,
    val musicState: MusicState = MusicState.NONE,
    val loading: Boolean = false,
    val error: String = "",
    val ready: Boolean = false,
    val musicList: List<Music> = emptyList(),
    val searchValue: String = ""
)

enum class MusicState {
    PLAYING, PAUSED, NONE
}
