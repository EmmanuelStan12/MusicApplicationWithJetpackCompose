package com.cd.musicplayerapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.ui.theme.Black
import com.cd.musicplayerapp.ui.theme.Light
import com.google.accompanist.pager.ExperimentalPagerApi
import timber.log.Timber

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ExpandedSheetContent(
    music: Music,
    modifier: Modifier = Modifier,
    collapse: () -> Unit
) {

    Column(
        modifier = modifier,
    ) {
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
        ExpandedSheetItem(music = music)
    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ExpandedSheetItem(
    music: Music
) {
    val painter = rememberImagePainter(music.bitmap)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(Modifier.height(10.dp))
        Text(text = music.album, style = MaterialTheme.typography.h4)
        Spacer(Modifier.height(10.dp))
        when (painter.state) {
            is ImagePainter.State.Loading -> {
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
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }
            }
            is ImagePainter.State.Error -> {
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
            is ImagePainter.State.Empty -> {
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
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
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
    }
}