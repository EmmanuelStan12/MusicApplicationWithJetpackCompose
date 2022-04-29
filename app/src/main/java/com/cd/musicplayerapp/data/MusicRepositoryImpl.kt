package com.cd.musicplayerapp.data

import android.content.Context
import com.cd.musicplayerapp.domain.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val dao: MusicDao,
    @ApplicationContext private val context: Context
): MusicRepository {

    override fun searchSongs(query: String): Flow<List<Music>> = dao.searchSongs(query).map { list ->
        list.map { it.toMusic(context) }
    }

    override suspend fun getAllSongs(): List<Music> = dao.getAllSongs().map { it.toMusic(context) }

    override suspend fun getSongById(id: String): Music = dao.getSongById(id).toMusic(context)

    override suspend fun insertSongs(songs: List<Music>) = dao.insertSongs(songs.map { it.toMusicEntity() })

    override suspend fun insertSong(music: Music): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        dao.insertSong(music.toMusicEntity())
        emit(Resource.Success(data = Unit))
    }

    override suspend fun deleteSong(song: Music) = dao.deleteSong(song.toMusicEntity())

    override suspend fun deleteAllSongs() = dao.deleteAllSongs()

    override fun searchPlaylist(query: String): Flow<List<Playlist>> =
        dao.searchPlaylist(query).map {
            it.map { entity -> entity.toPlaylist() }
        }

    override suspend fun getAllPlaylists(): List<Playlist> = dao.getAllPlaylists().map {
        it.toPlaylist()
    }

    override suspend fun getPlaylistByTitle(title: String): Playlist =
        dao.getPlaylistByTitle(title).toPlaylist()

    override suspend fun insertPlaylists(playlists: List<Playlist>) {

    }

    override suspend fun insertPlaylist(playlist: Playlist): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        dao.insertPlaylist(playlist.toPlaylistEntity())
        emit(Resource.Success(data = Unit))
    }
}