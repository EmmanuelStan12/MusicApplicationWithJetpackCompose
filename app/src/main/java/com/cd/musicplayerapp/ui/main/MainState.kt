package com.cd.musicplayerapp.ui.main

import android.support.v4.media.session.PlaybackStateCompat
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.domain.Playlist

data class MainState(
    val currentPlayingMusic: Music? = null,
    val musicState: MusicState = MusicState.NONE,
    val loading: Boolean = false,
    val error: String = "",
    val ready: Boolean = false,
    val musicList: List<Music> = emptyList(),
    val searchValue: String = "",
    val repeatMode: RepeatMode = RepeatMode.RepeatModeAll,
    val currentSelectedAlbum: String = "",
    val albumMusicList: List<Playlist> = emptyList()
) {
    val albumFilteredList: List<Music>
        get() = musicList.filter {
            it.album == currentSelectedAlbum
        }
}

enum class MusicState {
    PLAYING, PAUSED, NONE
}

sealed class RepeatMode(
    val playbackState: Int
) {
    object RepeatModeAll: RepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)
    object RepeatModeOne: RepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
    object RepeatModeNone: RepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)
    object ShuffleModeAll: RepeatMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
}

val repeatModeList = listOf<RepeatMode>(
    RepeatMode.RepeatModeAll,
    RepeatMode.RepeatModeOne,
    RepeatMode.RepeatModeNone,
    RepeatMode.ShuffleModeAll
)


