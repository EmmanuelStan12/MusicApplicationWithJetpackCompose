package com.cd.musicplayerapp.ui.playlist_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.ui.home.musicItems
import com.cd.musicplayerapp.ui.main.MusicState
import com.cd.musicplayerapp.ui.theme.Light
import kotlinx.coroutines.CoroutineScope
import java.util.*

@Composable
fun PlaylistScreen(
    album: String,
    data: List<Music>,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    currentPlayingMusic: Music?,
    scope: CoroutineScope,
    musicState: MusicState,
    onPlayPausePressed: (Music) -> Unit,
    onClick: (Music) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(top = 10.dp, start = 15.dp, end = 15.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 10.dp)
            ) {
                musicItems(
                    data,
                    searchValue,
                    onSearchValueChanged,
                    musicState,
                    currentPlayingMusic,
                    onPlayPausePressed,
                    onClick,
                    scope,
                    playlistTitle = album.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    },
                    playlistSubtitle = "Well, what do we have hear? \uD83E\uDD14"
                )
            }
        }
    }
}