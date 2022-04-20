package com.cd.musicplayerapp.di

import com.cd.musicplayerapp.data.MusicRepositoryImpl
import com.cd.musicplayerapp.domain.MusicRepository
import com.cd.musicplayerapp.exoplayer.LocalMediaSource
import com.cd.musicplayerapp.exoplayer.MusicSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {

    @Binds
    abstract fun bindMusicSource(
        localMediaSource: LocalMediaSource
    ): MusicSource

    @Binds
    abstract fun bindMusicRepository(
        musicRepositoryImpl: MusicRepositoryImpl
    ): MusicRepository
}