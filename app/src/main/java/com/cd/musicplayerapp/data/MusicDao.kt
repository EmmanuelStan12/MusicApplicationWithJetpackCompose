package com.cd.musicplayerapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Query("SELECT * FROM music_entity")
    fun getAllSongs(): List<MusicEntity>

    @Query("SELECT * FROM music_entity WHERE title LIKE '%' || :query || '%' OR artists LIKE '%' || :query || '%' ")
    fun searchSongs(query: String): Flow<List<MusicEntity>>

    @Query("SELECT * FROM music_entity WHERE _id = :id")
    suspend fun getSongById(id: String): MusicEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<MusicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(music: MusicEntity)

    @Query("DELETE FROM music_entity")
    suspend fun deleteAllSongs()

    @Delete
    suspend fun deleteSong(song: MusicEntity)

    suspend fun cacheSongs(songs: List<MusicEntity>) {
        deleteAllSongs()
        insertSongs(songs)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlistentity")
    fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlistentity WHERE title LIKE '%' || :query || '%' ")
    fun searchPlaylist(query: String): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlistentity WHERE title = :title")
    suspend fun getPlaylistByTitle(title: String): PlaylistEntity
}