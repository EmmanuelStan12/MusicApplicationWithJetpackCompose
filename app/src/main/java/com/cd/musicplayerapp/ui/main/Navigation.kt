package com.cd.musicplayerapp.ui.main

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cd.musicplayerapp.ui.Screen
import com.cd.musicplayerapp.ui.details.BottomSheetContent
import com.cd.musicplayerapp.ui.home.HomeScreen
import com.cd.musicplayerapp.ui.playlist_detail_screen.PlaylistDetailScreen
import com.cd.musicplayerapp.ui.playlist_screen.PlaylistScreen
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Navigation(viewModel: MainViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val colors = MaterialTheme.colors

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val scaffoldState = rememberBottomSheetScaffoldState()

    var isExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state.musicState, key2 = viewModel.shouldUpdateSeekbarPosition.value) {
        viewModel.updateCurrentPlayerPosition()
    }

    LaunchedEffect(key1 = scaffoldState.bottomSheetState.isExpanded) {
        Timber.d("sheet state ${scaffoldState.bottomSheetState.isExpanded}")

        isExpanded = scaffoldState.bottomSheetState.isExpanded
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            BottomSheetContent(
                state = state,
                onPlayPausePressed = viewModel::onPlayPauseButtonPressed,
                seekTo = viewModel::seekTo,
                onValueChanged = viewModel::onValueChanged,
                currentPlayerPosition = viewModel.currentPlayerPosition.value,
                onNextPrevClicked = viewModel::onNextPrevClicked,
                onRepeatStateChanged = viewModel::onRepeatStateChanged,
                isExpanded = isExpanded,
                onBottomSheetStateChanged = { expand ->
                    if(expand) {
                        scope.launch {
                            scaffoldState.bottomSheetState.snapTo(BottomSheetValue.Expanded)
                        }
                    } else {
                        scope.launch {
                            scaffoldState.bottomSheetState.snapTo(BottomSheetValue.Collapsed)
                        }
                    }
                }
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
                        if (it._mediaId == state.currentPlayingMusic?._mediaId) {
                            scope.launch {
                                scaffoldState.bottomSheetState.snapTo(BottomSheetValue.Expanded)
                            }
                        } else {
                            viewModel.onPlayPauseButtonPressed(it)
                            scope.launch {
                                scaffoldState.bottomSheetState.snapTo(BottomSheetValue.Expanded)
                            }
                        }
                    },
                    navController = navController
                )
            }
            composable(Screen.PlaylistDetailScreen.route) {
                PlaylistDetailScreen(
                    album = state.currentSelectedAlbum,
                    data = state.albumMusicList,
                    searchValue = "",
                    onSearchValueChanged = {},
                    scope = scope,
                    onPlayPausePressed = {
                        viewModel.subscribeToMusicFromAlbumTitle(it.title)
                    },
                    onClick = {
                        navController.navigate("${Screen.PlaylistScreen.route}/${it.title}")
                    }
                )
            }
            composable(
                "${Screen.PlaylistScreen.route}/{album}",
                arguments = listOf(navArgument("album") { type = NavType.StringType })
            ) { backStackEntry ->
                viewModel.changeAlbum(backStackEntry.arguments?.getString("album") ?: "")
                PlaylistScreen(
                    album = backStackEntry.arguments?.getString("album") ?: "",
                    data = state.albumFilteredList,
                    searchValue = state.searchValue,
                    onSearchValueChanged = viewModel::onSearchQueryChanged,
                    currentPlayingMusic = state.currentPlayingMusic,
                    scope = scope,
                    musicState = state.musicState,
                    onPlayPausePressed = viewModel::onPlayPauseButtonPressed,
                    onClick = {
                        if (it._mediaId == state.currentPlayingMusic?._mediaId) {
                            scope.launch {
                                scaffoldState.bottomSheetState.snapTo(BottomSheetValue.Expanded)
                            }
                        } else {
                            viewModel.onPlayPauseButtonPressed(it)
                            scope.launch {
                                scaffoldState.bottomSheetState.snapTo(BottomSheetValue.Expanded)
                            }
                        }
                    }
                )
            }

        }
    }
}
