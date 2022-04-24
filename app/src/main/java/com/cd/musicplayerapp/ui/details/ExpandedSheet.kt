package com.cd.musicplayerapp.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.exoplayer.timeInMinutes
import com.cd.musicplayerapp.ui.theme.Black
import com.cd.musicplayerapp.ui.theme.Light

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ExpandedSheet(
    music: Music,
    isPlaying: Boolean,
    onPlayPauseClick: (Boolean, Music) -> Unit,
    onPrevNextClick: (Boolean) -> Unit,
    onValueChangedFinished: (Long) -> Unit,
    currentPlayerPosition: Long,
    onFastForwardRewindClick: (Boolean) -> Unit,
    valueChanging: () -> Unit,
    collapse: () -> Unit,
) {

    val painter = rememberImagePainter(music.imageUri)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.3f)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Light.copy(alpha = 0.5f)
                    )
            )
            IconButton(onClick = collapse, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = Light
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(text = music.album, style = MaterialTheme.typography.h4)
        Spacer(Modifier.height(10.dp))
        when (painter.state) {
            is ImagePainter.State.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.5f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Black),
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.5f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Black),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_headphones),
                        contentDescription = null,
                        tint = Light,
                        modifier = Modifier
                            .fillMaxSize(0.8f)
                            .align(Alignment.Center)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = music.title,
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(horizontal = 15.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = music.artists.joinToString(" "),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(horizontal = 15.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Seeker(
            music = music,
            isPlaying = isPlaying,
            onPlayPauseClick = onPlayPauseClick,
            onFastForwardRewindClick = onFastForwardRewindClick,
            onPrevNextClick = onPrevNextClick,
            currentPlayerPosition = currentPlayerPosition,
            onValueChangedFinished = onValueChangedFinished,
            valueChanging = valueChanging
        )
    }

}

@Composable
fun Seeker(
    music: Music,
    isPlaying: Boolean,
    onPlayPauseClick: (Boolean, Music) -> Unit,
    onPrevNextClick: (Boolean) -> Unit,
    onFastForwardRewindClick: (Boolean) -> Unit,
    currentPlayerPosition: Long,
    onValueChangedFinished: (Long) -> Unit,
    valueChanging: () -> Unit
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
        IconButton(onClick = { onPrevNextClick(false) }) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.ic_previous),
                contentDescription = null,
                tint = Light
            )
        }
        IconButton(onClick = { onFastForwardRewindClick(false) }) {
            Icon(
                modifier = Modifier.size(37.dp),
                painter = painterResource(id = R.drawable.ic_rewind),
                contentDescription = null,
                tint = Light
            )
        }
        IconButton(onClick = { onPlayPauseClick(isPlaying, music) }) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = null,
                tint = Light
            )
        }
        IconButton(onClick = { onFastForwardRewindClick(true) }) {
            Icon(
                modifier = Modifier.size(33.dp),
                painter = painterResource(id = R.drawable.ic_fast_forward),
                contentDescription = null,
                tint = Light
            )
        }
        IconButton(onClick = { onPrevNextClick(true) }) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                tint = Light
            )
        }
    }
}