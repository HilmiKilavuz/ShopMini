package com.example.shopmini.di

import com.example.shopmini.data.repository.AddressRepositoryImpl
import com.example.shopmini.domain.repository.AddressRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//Address repository sınıfının Hilt modülüdür.
@Module
@InstallIn(SingletonComponent::class)
abstract class AddressModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AddressRepositoryImpl): AddressRepository
}