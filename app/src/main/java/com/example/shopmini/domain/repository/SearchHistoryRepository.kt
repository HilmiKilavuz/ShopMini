package com.example.shopmini.domain.repository

import com.example.shopmini.data.local.SearchHistoryEntity
import com.example.shopmini.data.model.Product
import kotlinx.coroutines.flow.Flow
//Arama geçmişi ile alakalı işlemleri burada yapıyoruz
interface SearchHistoryRepository {
     fun getRecentSearches(): Flow<List<SearchHistoryEntity>>
    suspend fun insertSearch(entity: SearchHistoryEntity)
    suspend fun deleteSearch(entity: SearchHistoryEntity)

    suspend fun searchProducts(query: String): List<Product>
}
