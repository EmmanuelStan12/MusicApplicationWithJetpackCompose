package com.cd.musicplayerapp.ui.details

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.domain.emptyMusic
import com.cd.musicplayerapp.ui.main.MainState
import com.cd.musicplayerapp.ui.main.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColumnScope.BottomSheetContent(
    state: MainState,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onPlayPausePressed: (Music) -> Unit,
    scope: CoroutineScope,
    seekTo: (Long) -> Unit,
    onValueChanged: () -> Unit,
    currentPlayerPosition: Long,
    onNextPrevClicked: (Boolean) -> Unit
) {

    val context = LocalContext.current

    AnimatedVisibility(
        visible = bottomSheetScaffoldState.bottomSheetState.isCollapsed
    ) {
        CollapsedSheet(
            music = state.currentPlayingMusic ?: run {
                if (state.musicList.isNotEmpty()) state.musicList[0]
                else emptyMusic
            },
            isPlaying = state.musicState == MusicState.PLAYING,
            onPlayPauseClick = { _, music ->
                if (music._mediaId.isNotBlank()) onPlayPausePressed(music)
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
                if (state.musicList.isNotEmpty()) state.musicList[0]
                else emptyMusic
            },
            onValueChangedFinished = seekTo,
            currentPlayerPosition = currentPlayerPosition,
            valueChanging = onValueChanged,
            onPlayPauseClick = { _, music ->
                onPlayPausePressed(music)
            },
            onPrevNextClick = {
                onNextPrevClicked(it)
            },
            onFastForwardRewindClick = { },
            isPlaying = state.musicState == MusicState.PLAYING
        ) {
            scope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }
}