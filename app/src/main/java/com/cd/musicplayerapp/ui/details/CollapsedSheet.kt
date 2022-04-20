package com.cd.musicplayerapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.ui.theme.Black
import com.cd.musicplayerapp.ui.theme.Light

@Composable
fun CollapsedSheet(
    expand: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    tint = Light,
                    contentDescription = null
                )
            }
            IconButton(onClick = expand) {
                Icon(
                    modifier = Modifier.rotate(180f),
                    imageVector = Icons.Outlined.ArrowDropDown,
                    tint = Light,
                    contentDescription = null
                )
            }
        }
    }

}