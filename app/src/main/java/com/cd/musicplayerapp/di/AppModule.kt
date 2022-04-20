package com.cd.musicplayerapp.di

import android.content.Context
import androidx.room.Room
import com.cd.musicplayerapp.data.MusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        MusicDatabase::class.java,
        "music_database"
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideMusicDao(database: MusicDatabase) = database.musicDao
}