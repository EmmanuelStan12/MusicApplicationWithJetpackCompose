package com.cd.musicplayerapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cd.musicplayerapp.ui.components.MusicItem
import com.cd.musicplayerapp.ui.components.Searchbar
import com.cd.musicplayerapp.ui.details.CollapsedSheet
import com.cd.musicplayerapp.ui.details.ExpandedSheet
import com.cd.musicplayerapp.ui.theme.BBFontFamily
import com.cd.musicplayerapp.ui.theme.Light
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val colors = MaterialTheme.colors

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetPeekHeight = 70.dp,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            AnimatedVisibility(
                visible = bottomSheetScaffoldState.bottomSheetState.isCollapsed,
            ) {
                CollapsedSheet {
                    Timber.d("Expand now")
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                }
            }
            AnimatedVisibility(
                visible = bottomSheetScaffoldState.bottomSheetState.isExpanded,
            ) {
                ExpandedSheet {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }
        }
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
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 10.dp)
                ) {
                    item {
                        Text(
                            text = "Hello",
                            fontFamily = BBFontFamily,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Light
                        )
                        Text(
                            text = "What you want to hear today?",
                            style = MaterialTheme.typography.body1
                        )
                        Searchbar(
                            value = "",
                            onValueChange = {}
                        )

                    }
                    items(state.data.size) { index ->
                        val music = state.data[index]
                        MusicItem(
                            music = music,
                            isPlaying = music._mediaId == state.currentPlayingMusic?._mediaId && state.musicState == MusicState.PLAYING,
                            onPlayClick = { it, isPlaying ->
                                if (!isPlaying) viewModel.onMusicListItemPressed(it)
                                else viewModel.onPlayPauseButtonPressed(it)
                            },
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}