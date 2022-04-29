package com.cd.musicplayerapp.domain

import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun searchSongs(query: String = ""): Flow<List<Music>>

    suspend fun getAllSongs(): List<Music>

    suspend fun getSongById(id: String): Music

    suspend fun insertSongs(songs: List<Music>)

    suspend fun insertSong(music: Music): Flow<Resource<Unit>>

    suspend fun deleteSong(song: Music)

    suspend fun deleteAllSongs()

    fun searchPlaylist(query: String = ""): Flow<List<Playlist>>

    suspend fun getAllPlaylists(): List<Playlist>

    suspend fun getPlaylistByTitle(title: String): Playlist

    suspend fun insertPlaylists(playlists: List<Playlist>)

    suspend fun insertPlaylist(playlist: Playlist): Flow<Resource<Unit>>

}