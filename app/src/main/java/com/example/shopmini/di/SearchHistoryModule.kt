package com.example.shopmini.di

import com.example.shopmini.data.repository.SearchHistoryRepositoryImpl
import com.example.shopmini.domain.repository.SearchHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SearchHistoryModule {
    @Binds
    @Singleton
    abstract fun provideSearchHistoryRepository(impl: SearchHistoryRepositoryImpl): SearchHistoryRepository

}