package com.cd.musicplayerapp.ui

sealed class Screen(
    val route: String
) {
    object Home: Screen("home")

    object HomePlayer: Screen("home_player")
}
