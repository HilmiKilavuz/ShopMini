package com.example.shopmini.di

import com.example.shopmini.data.local.AppDatabase
import com.example.shopmini.data.local.FavoriteDao
import com.example.shopmini.data.repository.FavoriteRepositoryImpl
import com.example.shopmini.domain.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteModule {

    // FavoriteRepository interface'ini FavoriteRepositoryImpl sınıfına bağlar
    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl)
    : FavoriteRepository



}