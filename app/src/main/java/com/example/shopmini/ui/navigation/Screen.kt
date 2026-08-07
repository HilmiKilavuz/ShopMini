package com.example.shopmini.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Navigasyon  Rotalarını tutar.
 * Type-Safe Navigation özelliği sayesinde ekran isimlerini string yerine,
 * bu şekilde güvenli sınıflar (class/object) olarak tanımlarız.
 */
sealed class Screen {
    // Ana sayfa rotası, herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object Home

    // Detay sayfası rotası, zorunlu olarak bir 'productId' parametresi alır.
    @Serializable
    data class ProductDetailScreen(val productId: Int)

    // Sepet sayfası rotası, şimdilik herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object Cart
    // Profil sayfası rotası,şimdilik herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object Profile
    // Favori sayfası rotası, şimdilik herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object Favorites
    // Giriş sayfası rotası, şimdilik herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object Login
    // Kayıt sayfası rotası, şimdilik herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object SignUp
    // Adreslerim sayfası rotası, şimdilik herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object Addresses
    // Yeni adres ekleme sayfası rotası
    @Serializable
    data class AddEditAddress(val addressId: String? = null)
    // Ödeme sayfası rotası, şimdilik herhangi bir parametreye ihtiyacı yoktur.
    @Serializable
    object Checkout



}