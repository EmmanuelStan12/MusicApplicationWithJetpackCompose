package com.cd.musicplayerapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MusicEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MusicDatabase : RoomDatabase() {

    abstract val musicDao: MusicDao
}