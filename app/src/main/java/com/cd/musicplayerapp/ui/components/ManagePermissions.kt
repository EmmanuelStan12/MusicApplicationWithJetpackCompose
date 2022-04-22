package com.cd.musicplayerapp.ui.components

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cd.musicplayerapp.ui.theme.Black
import com.cd.musicplayerapp.ui.theme.Light
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)
@Composable
fun ExternalStoragePermission(
    content: @Composable () -> Unit,
) {

    val readPermission =
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val writePermission =
        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    var hasWritePermission by remember { mutableStateOf(minSdk29) }

    LaunchedEffect(key1 = writePermission.hasPermission) {
        hasWritePermission = writePermission.hasPermission || minSdk29
    }

    if (!readPermission.hasPermission) {
        Rationale(
            text = "Read Storage Request",
            onRequestPermission = {
                readPermission.launchPermissionRequest()
            }
        )
    }

    if (!writePermission.hasPermission && !minSdk29) {
        Rationale(
            text = "Write Storage Request",
            onRequestPermission = {
                readPermission.launchPermissionRequest()
            }
        )
    }

    if (readPermission.permissionRequested && !readPermission.hasPermission && !hasWritePermission) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Permissions not Granted", style = MaterialTheme.typography.h5, color = Black)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Light)
                    .clickable {  }
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Text(text = "Request Permission", style = MaterialTheme.typography.button, color = Black)
            }
        }
    }

    if (readPermission.hasPermission && hasWritePermission) {
        content()
    }

}

@Composable
fun Rationale(
    text: String,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(text = "Permission request", style = MaterialTheme.typography.h6)
        },
        text = {
            Text(text = text, style = MaterialTheme.typography.body1)
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Light)
                    .clickable { onRequestPermission() }
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Text(text = "Accept", style = MaterialTheme.typography.button, color = Black)
            }
        },
        dismissButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Light)
                    .clickable {  }
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Text(text = "Reject", style = MaterialTheme.typography.button, color = Black)
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}