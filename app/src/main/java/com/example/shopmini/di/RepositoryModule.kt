/**
 * DI Katmanı .
 * Biri ProductRepository istediğinde ona ProductRepositoryImpl verilmesi gerektiğini söyler.
 */
package com.example.shopmini.di

import com.example.shopmini.data.repository.ProductRepositoryImpl
import com.example.shopmini.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Ürün için ProductModule sınıfı
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
}
