package com.cd.musicplayerapp.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cd.musicplayerapp.domain.emptyMusic
import com.cd.musicplayerapp.ui.components.MusicItem
import com.cd.musicplayerapp.ui.components.Searchbar
import com.cd.musicplayerapp.ui.details.CollapsedSheet
import com.cd.musicplayerapp.ui.details.ExpandedSheet
import com.cd.musicplayerapp.ui.theme.Light
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val colors = MaterialTheme.colors

    var offsetY by remember { mutableStateOf(70.dp.value) }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    val context = LocalContext.current

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        backgroundColor = Color.Transparent,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            AnimatedVisibility(
                visible = bottomSheetScaffoldState.bottomSheetState.isCollapsed
            ) {
                CollapsedSheet(
                    music = state.currentPlayingMusic ?: run {
                        if (state.data.isNotEmpty()) state.data[0]
                        else emptyMusic
                    },
                    isPlaying = state.musicState == MusicState.PLAYING,
                    onPlayPauseClick = { _, music ->
                        if (music._mediaId.isNotBlank()) viewModel.onPlayPauseButtonPressed(music)
                        else Toast.makeText(context, "No available songs", Toast.LENGTH_SHORT)
                            .show()
                    }
                ) {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                }
            }
            AnimatedVisibility(visible = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                ExpandedSheet(
                    music = state.currentPlayingMusic ?: run {
                        Timber.d("state home ${state.data.joinToString(" ,")}")
                        if (state.data.isNotEmpty()) state.data[0]
                        else emptyMusic
                    },
                    timePassed = state.timePassed,
                    onValueChanged = {
                        viewModel.seekTo(it)
                    },
                    onPlayPauseClick = { _, music -> viewModel.onPlayPauseButtonPressed(music) },
                    onPrevNextClick = {
                        viewModel.onNextPrevClicked(it)
                    },
                    onFastForwardRewindClick = {  },
                    isPlaying = state.musicState == MusicState.PLAYING
                ) {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }
        },
        sheetPeekHeight = 70.dp,
    ) {
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
                    contentPadding = PaddingValues(bottom = 10.dp)
                ) {
                    item {
                        Text(
                            text = "Hello \uD83D\uDC4B",
                            style = MaterialTheme.typography.h3
                        )
                        Text(
                            text = "What you want to hear today?",
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(Modifier.height(10.dp))
                        Searchbar(
                            value = "",
                            onValueChange = {}
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    items(state.data.size) { index ->
                        val music = state.data[index]
                        MusicItem(
                            music = music,
                            isPlaying = music._mediaId == state.currentPlayingMusic?._mediaId && state.musicState == MusicState.PLAYING,
                            onPlayClick = { it, isPlaying ->
                                viewModel.onPlayPauseButtonPressed(it)
                            },
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}
