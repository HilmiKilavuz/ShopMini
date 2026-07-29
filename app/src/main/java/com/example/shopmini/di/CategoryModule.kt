/**
 * DI Katmanı .
 * Biri CategoryRepository istediğinde ona CategoryRepositoryImpl verilmesi gerektiğini söyler.
 */
package com.example.shopmini.di

import com.example.shopmini.data.repository.CategoryRepositoryImpl
import com.example.shopmini.domain.repository.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Kategori için CategoryModule sınıfı
@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryModule {

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository


}
