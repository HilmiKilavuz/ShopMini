package com.example.shopmini.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Navigasyon (Ekranlar Arası Geçiş) Rotalarını tutar.
 * Type-Safe Navigation özelliği sayesinde ekran isimlerini string ("home") yerine,
 * bu şekilde güvenli sınıflar (class/object) olarak tanımlarız.
 */
sealed class Screen {
    // Ana sayfa rotası, herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object Home

    // Detay sayfası rotası, zorunlu olarak bir 'productId' parametresi alır.
    @Serializable
    data class ProductDetailScreen(val productId: Int)

}