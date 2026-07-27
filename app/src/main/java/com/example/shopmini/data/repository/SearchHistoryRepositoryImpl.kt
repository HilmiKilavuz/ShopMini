package com.example.shopmini.data.repository

import com.example.shopmini.data.local.SearchHistoryDao
import com.example.shopmini.data.local.SearchHistoryEntity
import com.example.shopmini.data.model.Product
import com.example.shopmini.data.remote.ShopMiniApi
import com.example.shopmini.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Arama geçmişi ile alakalı işlemleri burada yapıyoruz
class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
    private val api: ShopMiniApi
): SearchHistoryRepository {
    //Arama geçmişini döndürüyor
    override fun getRecentSearches(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getRecentSearches()


    }
    //Arama geçmişine ekleme yapıyor

    override suspend fun insertSearch(entity: SearchHistoryEntity) {
        searchHistoryDao.insertSearch(entity)


    }
//Arama geçmişinden silme yapıyor
    override suspend fun deleteSearch(entity: SearchHistoryEntity) {
        searchHistoryDao.deleteSearch(entity)


    }
    //Ürünü arama yapıyor

    override suspend fun searchProducts(query: String): List<Product> {
       return api.searchProducts(query).products
    }


}