package com.cd.musicplayerapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.domain.Music
import com.cd.musicplayerapp.ui.theme.BBFontFamily
import com.cd.musicplayerapp.ui.theme.Black
import com.cd.musicplayerapp.ui.theme.Light
import timber.log.Timber

@Composable
fun CollapsedSheet(
    music: Music,
    isPlaying: Boolean,
    onPlayPauseClick: (Boolean, Music) -> Unit,
    expand: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onPlayPauseClick(isPlaying, music)
            }) {
                Icon(
                    painter = painterResource(id = if(!isPlaying) R.drawable.ic_play else R.drawable.ic_pause),
                    tint = Light,
                    modifier = Modifier.size(25.dp),
                    contentDescription = null
                )
            }
            Spacer(Modifier.height(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = music.title ?: "",
                    fontSize = 30.sp,
                    fontFamily = BBFontFamily,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = music.artists.joinToString(" ,"),
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(10.dp))
            IconButton(onClick = expand) {
                Icon(
                    modifier = Modifier
                        .rotate(180f)
                        .size(25.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    tint = Light,
                    contentDescription = null,
                )
            }
        }
    }

}