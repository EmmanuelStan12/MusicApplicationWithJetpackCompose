package com.cd.musicplayerapp.ui.main

import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.domain.MusicRepository
import com.cd.musicplayerapp.domain.MusicUseCase
import com.cd.musicplayerapp.domain.Resource
import com.cd.musicplayerapp.exoplayer.currentPlaybackPosition
import com.cd.musicplayerapp.exoplayer.getMusicState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MusicUseCase,
    private val repository: MusicRepository
) : ViewModel() {

    private val _state = mutableStateOf(MainState())

    val state: State<MainState> get() = _state

    private val currentSong = useCase.currentSong

    private val playBackState = useCase.playbackState

    private val _currentPlayerPosition = mutableStateOf(0L)
    val currentPlayerPosition: State<Long> = _currentPlayerPosition

    val shouldUpdateSeekbarPosition = mutableStateOf(true)

    private var job: Job? = null

    init {
        subscribeToMusic()
        collectCurrentSong()
        collectPlayBackState()
    }

    fun updateCurrentPlayerPosition() = viewModelScope.launch {
        while (state.value.musicState == MusicState.PLAYING && shouldUpdateSeekbarPosition.value) {
            val position = playBackState.value?.currentPlaybackPosition
            if (position != currentPlayerPosition.value) {
                _currentPlayerPosition.value = position ?: 0L
            }
            delay(100L)
        }
    }

    fun changeAlbum(album: String) {
        _state.value = state.value.copy(currentSelectedAlbum = album)
    }

    fun onRepeatStateChanged() {
        val previousRepeatMode = state.value.repeatMode
        val previousRepeatModeIndex = repeatModeList.indexOf(previousRepeatMode)
        val currentRepeatModeIndex = if(previousRepeatModeIndex == repeatModeList.lastIndex)
            0 else previousRepeatModeIndex + 1
        if(currentRepeatModeIndex == repeatModeList.lastIndex) {
            useCase.changeRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)
            useCase.changeShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
        } else {
            useCase.changeRepeatMode(repeatModeList[currentRepeatModeIndex].playbackState)
            useCase.changeShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
        }
        _state.value = state.value.copy(repeatMode = repeatModeList[currentRepeatModeIndex])

    }

    fun playFromMediaId(music: Music) {
        useCase.playFromMediaId(music._mediaId)
    }

    fun seekTo(value: Long) = viewModelScope.launch {
        useCase.seekTo(value)
        _currentPlayerPosition.value = value
        delay(100L)
        shouldUpdateSeekbarPosition.value = true
    }

    private fun collectCurrentSong() = viewModelScope.launch {
        currentSong.collectLatest { metadata ->
            val music = metadata?.description?.mediaId?.let { repository.getSongById(it) }
            Timber.d("music id $music")

            _state.value = state.value.copy(currentPlayingMusic = music)
        }
    }

    private fun collectPlayBackState() = viewModelScope.launch {
        playBackState.collectLatest { playback ->
            val musicState = playback?.getMusicState() ?: MusicState.NONE
            _state.value = state.value.copy(
                musicState = musicState,
            )
        }
    }

    fun onPlayPauseButtonPressed(music: Music) = viewModelScope.launch {
        useCase.playPause(music._mediaId, true)
    }

    fun subscribeToMusicFromAlbumTitle(title: String) {
        viewModelScope.launch {
            useCase.unsubscribeToService()
            val resource = useCase.subscribeToService(title)
            if(resource is Resource.Success) {
                collectCurrentSong()
            }
        }
    }

    private fun subscribeToMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(loading = true)
            }
            val resource = useCase.subscribeToService()
            when (resource) {
                is Resource.Success -> {
                    collectSongs()
                    collectPlaylists()
                }
                is Resource.Error -> Timber.d("error ${resource.error}")
                is Resource.Loading -> {
                    Timber.d("some thing else is happening")
                }
            }

        }
    }

    fun onValueChanged() {
        shouldUpdateSeekbarPosition.value = false
    }

    fun onNextPrevClicked(isNext: Boolean) {
        if (isNext) useCase.skipToNextTrack()
        else useCase.skipToPrevTrack()
    }

    private suspend fun collectSongs() {
        repository.getAllSongs().also {
            Timber.d("list ${it.joinToString(" ")}")
            _state.value = state.value.copy(musicList = it, loading = false)
        }
    }

    private suspend fun collectPlaylists() {
        repository.getAllPlaylists().also {
            _state.value = state.value.copy(albumMusicList = it)
        }
    }

    fun onMusicListItemPressed(music: Music) = viewModelScope.launch {
        useCase.playPause(music._mediaId)
    }

    fun onSearchQueryChanged(query: String) = viewModelScope.launch {
        _state.value = state.value.copy(searchValue = query)
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            repository.searchSongs(state.value.searchValue).collectLatest {
                _state.value = state.value.copy(musicList = it)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        useCase.unsubscribeToService()
    }
}