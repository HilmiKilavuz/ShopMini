package com.example.shopmini.di

import com.example.shopmini.data.repository.AuthRepositoryImpl
import com.example.shopmini.domain.repository.AuthRepository
import com.example.shopmini.domain.usecase.auth.UpdatePasswordUseCase
import com.example.shopmini.domain.usecase.auth.UpdateProfileUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Kimlik doğrulama (Auth) katmanı için Hilt DI bağlama modülü.
 *
 * Bu modülün görevi, [AuthRepository] arayüzünü talep eden bir sınıfa
 * (örn. UseCase'ler) hangi somut implementasyonun ([AuthRepositoryImpl])
 * verileceğini Hilt'e bildirmektir.
 *
 * Bu sayede UseCase veya ViewModel'lar, Supabase'e doğrudan bağımlı olmaz.
 * İleride Supabase yerine Firebase gibi farklı bir backend kullanılmak istenirse
 * sadece bu modül değiştirilir, diğer katmanlar hiç dokunulmadan kalır.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    /**
     * [AuthRepository] arayüzünü [AuthRepositoryImpl] somut sınıfına bağlar.
     *
     * @param impl Hilt tarafından otomatik oluşturulup enjekte edilen gerçek implementasyon.
     * @return [AuthRepository] arayüzü olarak sunulan implementasyon.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    companion object {
        @Provides
        fun provideUpdateProfileUseCase(repo: AuthRepository): UpdateProfileUseCase =
            UpdateProfileUseCase(repo)
        @Provides
        fun provideUpdatePasswordUseCase(repo: AuthRepository): UpdatePasswordUseCase =
            UpdatePasswordUseCase(repo)
    }
}