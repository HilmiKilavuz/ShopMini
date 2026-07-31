package com.example.shopmini.di

import com.example.shopmini.data.repository.CartRepositoryImpl
import com.example.shopmini.domain.repository.CartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//CartModule sınıfı
@Module
@InstallIn(SingletonComponent::class)
abstract class CartModule {

//CartRepository interface'ini CartRepositoryImpl sınıfına bağlar
    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}