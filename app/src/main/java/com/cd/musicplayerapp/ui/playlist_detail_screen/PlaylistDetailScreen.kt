package com.cd.musicplayerapp.ui.playlist_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.domain.Playlist
import com.cd.musicplayerapp.ui.components.MusicItem
import com.cd.musicplayerapp.ui.components.PlaylistItem
import com.cd.musicplayerapp.ui.components.Searchbar
import com.cd.musicplayerapp.ui.main.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PlaylistDetailScreen(
    album: String,
    data: List<Playlist>,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    scope: CoroutineScope,
    onPlayPausePressed: (Playlist) -> Unit,
    onClick: (Playlist) -> Unit
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
                item {
                    Text(
                        text = "Playlists",
                        style = MaterialTheme.typography.h3
                    )
                    Spacer(Modifier.height(10.dp))
                    Searchbar(
                        value = searchValue,
                        onValueChange = onSearchValueChanged
                    )
                    Spacer(Modifier.height(10.dp))
                }
                items(data.size) { index ->
                    val playlist = data[index]
                    PlaylistItem(
                        playlist = playlist,
                        onPlayClick = { it, _ ->
                            onPlayPausePressed(it)
                        },
                        onClick = {
                            scope.launch {
                                onClick(it)
                                onPlayPausePressed(it)
                            }
                        },
                        bottomPadding = if (index == data.size - 1) 90.dp else 10.dp,
                    )
                }
            }
        }
    }
}

