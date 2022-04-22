package com.cd.musicplayerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cd.musicplayerapp.ui.Screen
import com.cd.musicplayerapp.ui.components.ExternalStoragePermission
import com.cd.musicplayerapp.ui.home.HomeScreen
import com.cd.musicplayerapp.ui.home.HomeViewModel
import com.cd.musicplayerapp.ui.theme.MusicPlayerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerAppTheme {

                ExternalStoragePermission {
                    HomeScreen()
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