/**
 * Ürün işlemleri için oluşturulmuş Kurallar katmanı sözleşmesidir.
 * ViewModellar veriye ulaşmak için sadece bu sözleşmeye güvenirler.
 */
package com.example.shopmini.domain.repository

import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.data.model.Product

interface ProductRepository {
    /**
     * Tüm ürünleri sayfalı olarak getirir.
     *
     * @param limit Bir sayfada gösterilecek maksimum ürün sayısı (varsayılan: 20)
     * @param skip Kaç ürünün atlanacağı (sayfalama için, varsayılan: 0)
     * @return Ürün listesi
     */
    suspend fun getProducts(limit: Int = 20, skip: Int = 0): List<Product>

    /**
     * Belirli bir kategoriye ait ürünleri getirir.
     *
     * @param slug Kategorinin benzersiz adı
     * @param limit Bir sayfada gösterilecek maksimum ürün sayısı
     * @param skip Kaç ürünün atlanacağı
     * @return İlgili kategoriye ait ürün listesi
     */
    suspend fun getProductsByCategory(slug: String, limit: Int = 20, skip: Int = 0): List<Product>

    suspend fun getProductById(id: Int): Product
}
