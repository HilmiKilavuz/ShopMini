/**
 * Kategori işlemleri için oluşturulmuş Kurallar katmanı sözleşmesidir.
 * Verinin nereden geleceğini bilmez, sadece 'Kategorileri getiren bir fonksiyon olmalı' der.
 */
package com.example.shopmini.domain.repository

import com.example.shopmini.data.model.CategoryDto

interface CategoryRepository {
    /**
     * Tüm ürün kategorilerini getirir.
     * @return Kategori DTO'larını içeren bir liste.
     */
    suspend fun getCategories(): List<CategoryDto>
}
