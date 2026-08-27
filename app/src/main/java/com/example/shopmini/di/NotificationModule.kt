package com.example.shopmini.di

import android.content.Context
import com.example.shopmini.ui.util.NotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationService(
        @ApplicationContext context: Context
    ): NotificationService {
        return NotificationService(context)
    }
}
