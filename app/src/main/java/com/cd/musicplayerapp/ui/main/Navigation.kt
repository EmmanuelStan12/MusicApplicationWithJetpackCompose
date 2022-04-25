package com.cd.musicplayerapp.ui.main

import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cd.musicplayerapp.ui.Screen
import com.cd.musicplayerapp.ui.details.BottomSheetContent
import com.cd.musicplayerapp.ui.home.HomeScreen
import com.cd.musicplayerapp.ui.playlist_screen.PlaylistScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Navigation(viewModel: MainViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val colors = MaterialTheme.colors

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(key1 = state.musicState, key2 = viewModel.shouldUpdateSeekbarPosition.value) {
        viewModel.updateCurrentPlayerPosition()
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        backgroundColor = Color.Transparent,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            BottomSheetContent(
                state = state,
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                onPlayPausePressed = viewModel::onPlayPauseButtonPressed,
                scope = scope,
                seekTo = viewModel::seekTo,
                onValueChanged = viewModel::onValueChanged,
                currentPlayerPosition = viewModel.currentPlayerPosition.value,
                onNextPrevClicked = viewModel::onNextPrevClicked
            )
        },
        sheetPeekHeight = 70.dp,
    ) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    loading = state.loading,
                    data = state.musicList,
                    searchValue = state.searchValue,
                    onSearchValueChanged = viewModel::onSearchQueryChanged,
                    currentPlayingMusic = state.currentPlayingMusic,
                    scope = scope,
                    musicState = state.musicState,
                    onPlayPausePressed = viewModel::onPlayPauseButtonPressed,
                    onClick = {
                        scope.launch {
                            if (it._mediaId == state.currentPlayingMusic?._mediaId) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else {
                                viewModel.onPlayPauseButtonPressed(it)
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
                    }
                )
            }

            composable(Screen.PlaylistScreen.route) {
                PlaylistScreen()
            }
        }
    }
}
