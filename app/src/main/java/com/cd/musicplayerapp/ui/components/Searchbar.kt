package com.cd.musicplayerapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cd.musicplayerapp.R
import com.cd.musicplayerapp.ui.theme.Black
import com.cd.musicplayerapp.ui.theme.Dark
import com.cd.musicplayerapp.ui.theme.Light

@Composable
fun Searchbar(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Black)
            .padding(horizontal = 10.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = null,
            tint = Light
        )
        Spacer(modifier = Modifier.width(7.dp))
        Box(modifier = Modifier.weight(1f)) {
            if(value.isBlank()) Text(text = "Search...", style = MaterialTheme.typography.button.copy(color = Light))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.button.copy(color = Light)
            )
        }
    }
}