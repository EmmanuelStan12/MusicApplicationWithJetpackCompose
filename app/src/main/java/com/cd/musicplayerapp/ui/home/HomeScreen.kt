package com.cd.musicplayerapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cd.musicplayerapp.ui.components.MusicItem
import com.cd.musicplayerapp.ui.components.Searchbar
import com.cd.musicplayerapp.ui.theme.Light
import timber.log.Timber

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val colors = MaterialTheme.colors

    Timber.d("HomeScreen ${state.data.joinToString(" ")}")
    Timber.d("HomeScreen $state")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(top = 10.dp, start = 15.dp, end = 15.dp),
        contentAlignment = Alignment.Center
    ) {

        if (state.loading) {
            CircularProgressIndicator(color = Light)
        }
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 10.dp)
            ) {
                item {
                    Text(text = "Hello", style = MaterialTheme.typography.h2)
                    Text(
                        text = "What you want to hear today?",
                        style = MaterialTheme.typography.body1
                    )
                    Searchbar()

                }
                items(state.data.size) { index ->
                    val music = state.data[index]
                    MusicItem(
                        music = music,
                        isPlaying = music._mediaId == state.currentPlayingMusic?._mediaId && state.musicState == MusicState.PLAYING,
                        onPlayClick = { it, isPlaying ->
                            if(!isPlaying) viewModel.onMusicListItemPressed(it)
                            else viewModel.onPlayPauseButtonPressed(it)
                        },
                        onClick = {}
                    )
                }
            }
        }
    }
}