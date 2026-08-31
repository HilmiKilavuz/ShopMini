/**
 * DI Katmanı.
 * CouponRepository arayüzünü CouponRepositoryImpl'e bağlayan Hilt modülü.
 */
package com.example.shopmini.di

import com.example.shopmini.data.repository.CouponRepositoryImpl
import com.example.shopmini.domain.repository.CouponRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CouponModule {

    @Binds
    @Singleton
    abstract fun bindCouponRepository(
        couponRepositoryImpl: CouponRepositoryImpl
    ): CouponRepository
}
