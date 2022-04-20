package com.cd.musicplayerapp.ui.home

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.domain.MusicRepository
import com.cd.musicplayerapp.domain.MusicUseCase
import com.cd.musicplayerapp.domain.Resource
import com.cd.musicplayerapp.exoplayer.getMusic
import com.cd.musicplayerapp.exoplayer.getMusicState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCase: MusicUseCase,
    private val repository: MusicRepository
) : ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())

    val state: State<HomeScreenState> get() = _state

    private val searchQuery = MutableStateFlow("")

    private val _uiEvents = MutableSharedFlow<HomeUiEvent>()

    val uiEvent = _uiEvents.asSharedFlow()
    private val currentSong = useCase.currentSong

    private val playBackState = useCase.playbackState

    init {
        subscribeToMusic()
        collectPlayBackState()
        collectCurrentSong()
    }


    private suspend fun collectSongs() {
        repository.getAllSongs().also {
            _state.value = state.value.copy(data = it, loading = false)
        }
    }

    private fun collectCurrentSong() = viewModelScope.launch {
        currentSong.collectLatest {
            val music = it?.getMusic()
            _state.value = state.value.copy(currentPlayingMusic = music)
        }
    }

    private fun collectPlayBackState() = viewModelScope.launch {
        playBackState.collectLatest { playback ->
            val musicState = playback?.getMusicState() ?: MusicState.NONE
            _state.value = state.value.copy(musicState = musicState)
        }
    }

    fun onMusicListItemPressed(music: Music) = viewModelScope.launch {
        useCase.playPause(music._mediaId)
    }

    fun onPlayPauseButtonPressed(music: Music) = viewModelScope.launch {
        useCase.playPause(music._mediaId, true)
    }

    fun onMusicBottomBarPressed(music: Music) = viewModelScope.launch {

    }

    fun onSearchQueryChanged(query: String) = viewModelScope.launch {
        _state.value = state.value.copy(searchString = query)
        searchQuery.emit(query)
    }

    fun onBottomBarDismissed() = viewModelScope.launch {
        useCase.stopPlaying()
    }

    private fun subscribeToMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("current thread is ${Thread.currentThread().name}")
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(loading = true)
            }
            val resource = useCase.subscribeToService()
            if (resource is Resource.Success){
                handleMusic(resource.data ?: emptyList())
                collectSongs()
            }

        }
    }

    private fun handleMusic(metaDataList: List<MediaBrowserCompat.MediaItem>) {}

    override fun onCleared() {
        super.onCleared()
        useCase.unsubscribeToService()
    }

}

sealed class HomeUiEvent {

}