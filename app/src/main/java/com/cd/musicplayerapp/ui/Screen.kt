package com.cd.musicplayerapp.ui

sealed class Screen(
    val route: String
) {
    object Home: Screen("home_screen")

    object PlaylistScreen: Screen("playlist_screen")

    object PlaylistDetailScreen: Screen("playlist_detail_screen")
}
