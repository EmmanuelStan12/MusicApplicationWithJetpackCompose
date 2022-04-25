package com.cd.musicplayerapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.data.MusicDatasource
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.exoplayer.loadMusicImageUri
import com.cd.musicplayerapp.exoplayer.timeInMinutes
import com.cd.musicplayerapp.ui.theme.Black
import com.cd.musicplayerapp.ui.theme.Light

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MusicItem(
    modifier: Modifier = Modifier,
    music: Music,
    isPlaying: Boolean,
    bottomPadding: Dp = 10.dp,
    onClick: (Music) -> Unit,
    onPlayClick: (Music, Boolean) -> Unit
) {

    val colors = MaterialTheme.colors
    val context = LocalContext.current
    val painter = rememberImagePainter(context.loadMusicImageUri(music.musicUri.toUri()))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Black)
            .clickable {
                onClick(music)
            }
            .padding(horizontal = 7.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Light),
            contentAlignment = Alignment.Center
        ) {
            when (painter.state) {
                is ImagePainter.State.Loading -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_noise),
                        contentDescription = null,
                        tint = colors.background,
                        modifier = Modifier.size(30.dp)
                    )
                }
                is ImagePainter.State.Success -> {
                    Image(painter = painter, contentDescription = null)
                }
                is ImagePainter.State.Error -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_noise),
                        contentDescription = null,
                        tint = colors.background,
                        modifier = Modifier.size(30.dp)
                    )
                }
                ImagePainter.State.Empty -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_noise),
                        contentDescription = null,
                        tint = colors.background,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.h4
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = music.artists.joinToString(" "),
                    style = MaterialTheme.typography.body1
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = music.duration.timeInMinutes, style = MaterialTheme.typography.body1)
                IconButton(onClick = {
                    onPlayClick(music, isPlaying)
                }) {
                    Icon(
                        painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                        contentDescription = null,
                        tint = Light,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Light.copy(alpha = 0.1f))
                    .clickable { }
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Text(text = music.album, style = MaterialTheme.typography.button, color = Light)
            }
        }
    }
    Spacer(modifier = Modifier.height(bottomPadding))

}