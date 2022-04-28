package com.cd.musicplayerapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.ui.Screen
import com.cd.musicplayerapp.ui.components.MusicItem
import com.cd.musicplayerapp.ui.components.Searchbar
import com.cd.musicplayerapp.ui.main.MusicState
import com.cd.musicplayerapp.ui.theme.Light
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    loading: Boolean = false,
    data: List<Music>,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    currentPlayingMusic: Music?,
    scope: CoroutineScope,
    musicState: MusicState,
    onPlayPausePressed: (Music) -> Unit,
    onClick: (Music) -> Unit,
    navController: NavController
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(top = 10.dp, start = 15.dp, end = 15.dp),
        contentAlignment = Alignment.Center
    ) {

        if (loading) {
            CircularProgressIndicator(color = Light)
        }
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
                    onAlbumClicked = {
                        navController.navigate("${Screen.PlaylistScreen.route}/${it}")
                    }
                )
            }
        }
    }
}

fun LazyListScope.musicItems(
    list: List<Music>,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    musicState: MusicState,
    currentPlayingMusic: Music?,
    onPlayPausePressed: (Music) -> Unit,
    onClick: (Music) -> Unit,
    scope: CoroutineScope,
    playlistTitle: String = "Hello \uD83D\uDC4B",
    playlistSubtitle: String = "What you want to hear today?",
    onAlbumClicked: ((String) -> Unit)? = null
) {
    item {
        Text(
            text = playlistTitle,
            style = MaterialTheme.typography.h3
        )
        Text(
            text = playlistSubtitle,
            style = MaterialTheme.typography.body1
        )
        Spacer(Modifier.height(10.dp))
        Searchbar(
            value = searchValue,
            onValueChange = onSearchValueChanged
        )
        Spacer(Modifier.height(10.dp))
    }
    items(list.size) { index ->
        val music = list[index]
        MusicItem(
            music = music,
            isPlaying = music._mediaId == currentPlayingMusic?._mediaId && musicState == MusicState.PLAYING,
            onPlayClick = { it, _ ->
                onPlayPausePressed(it)
            },
            onClick = {
                scope.launch {
                    onClick(it)
                    onPlayPausePressed(it)
                }
            },
            bottomPadding = if(index == list.size - 1) 90.dp else 10.dp,
            onAlbumClicked = onAlbumClicked
        )
    }
}