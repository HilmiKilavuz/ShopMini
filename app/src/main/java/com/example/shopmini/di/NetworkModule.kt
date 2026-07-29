/**
 * DI Katmanı .
 * Hilt'e Retrofit ve OkHttp  nesnelerini nasıl oluşturacağını öğretir.
 */
package com.example.shopmini.di

import com.example.shopmini.data.remote.ShopMiniApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
//Network için NetworkModule sınıfı
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * API'den dönen JSON verilerini Kotlin objelerine çevirmek için
     * kotlinx.serialization ayarlarını yapılandırır.
     */
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    /**
     * İnternet bağlantısını yönetecek Retrofit nesnesini oluşturur.
     * baseUrl olarak "https://dummyjson.com/" atanmıştır.
     */
    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    /**
     * Repository sınıflarının veri çekerken kullanacağı 
     * ShopMiniApi (İstek listesi) arayüzünü oluşturur.
     */
    @Provides
    @Singleton
    fun provideShopMiniApi(retrofit: Retrofit): ShopMiniApi {
        return retrofit.create(ShopMiniApi::class.java)
    }


}
