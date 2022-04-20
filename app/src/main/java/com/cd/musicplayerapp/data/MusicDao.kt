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
}