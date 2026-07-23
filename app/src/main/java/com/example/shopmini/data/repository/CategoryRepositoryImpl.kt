/**
 * DATA Katmanı .
 * CategoryRepository sözleşmesini uygular.
 * Kategorilerin gerçekten nereden getirileceğine burada karar verilir.
 */
package com.example.shopmini.data.repository

import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.data.remote.ShopMiniApi
import com.example.shopmini.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val api: ShopMiniApi) : CategoryRepository {
    /**
     * API üzerinden kategorileri getirir ve doğrudan UI'a iletilmesi için döndürür.
     */
    override suspend fun getCategories(): List<CategoryDto> {
        return api.getCategories()

    }
}
