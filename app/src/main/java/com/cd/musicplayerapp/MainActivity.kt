package com.cd.musicplayerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cd.musicplayerapp.receiver.MusicAlarmManager
import com.cd.musicplayerapp.ui.components.ExternalStoragePermission
import com.cd.musicplayerapp.ui.main.Navigation
import com.cd.musicplayerapp.ui.theme.MusicPlayerAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var musicAlarmManager: MusicAlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicAlarmManager.initialize()
        setContent {
            MusicPlayerAppTheme {

                ExternalStoragePermission {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicPlayerAppTheme {
        Greeting("Android")
    }
}