package com.cd.musicplayerapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cd.musicplayerapp.ui.theme.Black

@Composable
fun ExpandedSheet(
    collapse: () -> Unit
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Black)) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Welcome")
            IconButton(onClick = collapse) {
                Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = null)
            }
        }
    }

}