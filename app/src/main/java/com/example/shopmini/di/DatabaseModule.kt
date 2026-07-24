/**
 * DI Katmanı .
 * Hilt'e Room veritabanı  nesnelerini nasıl oluşturacağını öğretir.
 */
package com.example.shopmini.di

import android.content.Context
import androidx.room.Room
import com.example.shopmini.data.local.AppDatabase
import com.example.shopmini.data.local.ProductDao
import com.example.shopmini.data.local.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /**
     * Uygulama genelinde tek Singleton Room Veritabanı örneği sağlar.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return (Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "shopmini_db"
            ).fallbackToDestructiveMigration(false).build())
    }

    /**
     * Repository sınıflarının ihtiyaç duyduğu ProductDao nesnesini 
     * AppDatabase üzerinden oluşturarak Hilt'e sunar.
     */
    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }
    /**
     * Repository sınıflarının ihtiyaç duyduğu SearchHistoryDao nesnesini
     * AppDatabase üzerinden oluşturarak Hilt'e sunar.
     */
    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: AppDatabase): SearchHistoryDao {
        return database.searchHistoryDao()

    }

}
