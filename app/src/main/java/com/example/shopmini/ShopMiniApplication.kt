/**
 * Hilt'in (Dependency Injection) projeye dahil olduğu ilk giriş noktasıdır.
 * Tüm uygulama boyunca yaşayacak bağımlılıkların oluşturulmasını tetikler.
 */
package com.example.shopmini

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShopMiniApplication : Application()
