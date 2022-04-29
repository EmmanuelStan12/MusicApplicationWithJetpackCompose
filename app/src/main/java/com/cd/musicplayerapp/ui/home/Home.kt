package com.cd.musicplayerapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.ui.Screen
import com.cd.musicplayerapp.ui.components.MusicItem
import com.cd.musicplayerapp.ui.components.Searchbar
import com.cd.musicplayerapp.ui.main.MusicState
import com.cd.musicplayerapp.ui.theme.Black
import com.cd.musicplayerapp.ui.theme.Blue
import com.cd.musicplayerapp.ui.theme.Dark
import com.cd.musicplayerapp.ui.theme.Light
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

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

    val lazyListState = rememberLazyListState()
    LaunchedEffect(key1 = true) {
        lazyListState.interactionSource.interactions.collect {
            Timber.d("interaction ${it.toString()}")
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(top = 10.dp, start = 15.dp, end = 15.dp),
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(bottom = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.PlaylistDetailScreen.route)
                    },
                    backgroundColor = Blue,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_playlist),
                        contentDescription = null,
                        modifier = Modifier.size(34.dp),
                    )
                }
            }
        }
    ) {

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Light)
            }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 10.dp),
                state = lazyListState
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