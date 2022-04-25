package com.cd.musicplayerapp.ui.main

import com.cd.musicplayerapp.domain.Music

data class MainState(
    val currentPlayingMusic: Music? = null,
    val musicState: MusicState = MusicState.NONE,
    val loading: Boolean = false,
    val error: String = "",
    val ready: Boolean = false,
    val musicList: List<Music> = emptyList(),
    val searchValue: String = "",
    val repeatState: RepeatState = RepeatState.RepeatPlaylist
)

enum class MusicState {
    PLAYING, PAUSED, NONE
}

enum class RepeatState {
    RepeatPlaylist, RepeatCurrentSong, ShufflePlaylist, PlayPlaylistOnce
}
