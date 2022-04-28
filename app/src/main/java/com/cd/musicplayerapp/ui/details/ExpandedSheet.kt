package com.cd.musicplayerapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.exoplayer.timeInMinutes
import com.cd.musicplayerapp.ui.components.ExpandedSheetContent
import com.cd.musicplayerapp.ui.main.repeatModeList
import com.cd.musicplayerapp.ui.theme.Blue
import com.cd.musicplayerapp.ui.theme.Light
import com.cd.musicplayerapp.ui.theme.LightBlue
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalCoilApi::class, ExperimentalPagerApi::class)
@Composable
fun ExpandedSheet(
    currentPlayingMusic: Music,
    isPlaying: Boolean,
    onPlayPauseClick: (Boolean, Music) -> Unit,
    onPrevNextClick: (Boolean) -> Unit,
    onValueChangedFinished: (Long) -> Unit,
    currentPlayerPosition: Long,
    addToPlaylist: (Music) -> Unit,
    repeatMode: Int,
    onRepeatStateChanged: () -> Unit,
    valueChanging: () -> Unit,
    collapse: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(MaterialTheme.colors.background)
    ) {
        ExpandedSheetContent(
            modifier = Modifier.weight(1f),
            collapse = collapse,
            music = currentPlayingMusic
        )
        Spacer(Modifier.height(10.dp))
        Seeker(
            music = currentPlayingMusic,
            isPlaying = isPlaying,
            onPlayPauseClick = onPlayPauseClick,
            onPrevNextClick = onPrevNextClick,
            currentPlayerPosition = currentPlayerPosition,
            onValueChangedFinished = onValueChangedFinished,
            valueChanging = valueChanging,
            addToPlaylist = addToPlaylist,
            repeatState = repeatMode,
            onRepeatStateChanged = onRepeatStateChanged
        )
        Spacer(Modifier.height(15.dp))
    }

}

@Composable
fun Seeker(
    music: Music,
    isPlaying: Boolean,
    onPlayPauseClick: (Boolean, Music) -> Unit,
    onPrevNextClick: (Boolean) -> Unit,
    addToPlaylist: (Music) -> Unit,
    currentPlayerPosition: Long,
    onValueChangedFinished: (Long) -> Unit,
    valueChanging: () -> Unit,
    repeatState: Int,
    onRepeatStateChanged: () -> Unit
) {

    var seekbarValue by remember {
        mutableStateOf(currentPlayerPosition)
    }

    LaunchedEffect(key1 = currentPlayerPosition) {
        seekbarValue = currentPlayerPosition
    }

    Slider(
        value = seekbarValue.toFloat(),
        valueRange = 0F..music.duration.toFloat(),
        onValueChange = {
            seekbarValue = it.toLong()
            valueChanging()
        },
        colors = SliderDefaults.colors(
            thumbColor = Blue,
            activeTrackColor = Blue,
            inactiveTrackColor = LightBlue.copy(alpha = 0.3f),
        ),
        onValueChangeFinished = {
            onValueChangedFinished(seekbarValue)
        }
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(text = currentPlayerPosition.timeInMinutes, style = MaterialTheme.typography.body1)
        Text(text = music.duration.timeInMinutes, style = MaterialTheme.typography.body1)
    }
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        IconButton(onClick = {
            onRepeatStateChanged()
        }) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(
                    id = when(repeatState) {
                        repeatModeList[0] -> R.drawable.ic_repeat
                        repeatModeList[1] -> R.drawable.ic_repeat_one
                        repeatModeList[2] -> R.drawable.ic_play_next_song
                        repeatModeList[3] -> R.drawable.ic_shuffle_icon
                        else -> {
                            R.drawable.ic_repeat
                        }
                    }
                ),
                contentDescription = null,
                tint = Light
            )
        }
        IconButton(onClick = { onPrevNextClick(false) }) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_previous),
                contentDescription = null,
                tint = Light
            )
        }
        IconButton(onClick = { onPlayPauseClick(isPlaying, music) }) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = null,
                tint = Light
            )
        }
        IconButton(onClick = { onPrevNextClick(true) }) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                tint = Light
            )
        }
        IconButton(onClick = { addToPlaylist(music) }) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_add_playlist),
                contentDescription = null,
                tint = Light
            )
        }
    }
}