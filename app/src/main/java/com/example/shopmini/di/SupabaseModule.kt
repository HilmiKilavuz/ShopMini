package com.example.shopmini.di

import com.example.shopmini.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

/**
 * Supabase bağlantısını uygulamanın geneline sağlayan Hilt DI modülü.
 *
 * Bu modül, Supabase SDK'nın ana istemci nesnesini (SupabaseClient) oluşturur
 * ve Hilt aracılığıyla ihtiyaç duyan sınıflara (Repository gibi) enjekte eder.
 *
 * Bağlantı bilgileri (URL ve API Key) güvenli şekilde local.properties dosyasından
 * okunup BuildConfig aracılığıyla koda aktarılır.
 */
@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    /**
     * Supabase ana bağlantı nesnesini oluşturur ve sağlar.
     *
     * @return Uygulama boyunca tek bir instance olarak yaşayan [SupabaseClient].
     *
     * install(Auth)     → Giriş, kayıt, çıkış gibi kimlik doğrulama işlemleri için.
     * install(Postgrest) → 'profiles' gibi veritabanı tablolarına sorgu atmak için.
     */
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Auth)      // Kimlik doğrulama modülü
            install(Postgrest) // Veritabanı sorgu modülü
        }
    }
}