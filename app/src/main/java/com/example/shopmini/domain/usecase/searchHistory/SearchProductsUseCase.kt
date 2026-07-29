package com.example.shopmini.domain.usecase.searchHistory

import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.repository.SearchHistoryRepository
import javax.inject.Inject
//Arama sorgusunu yaparak ürünleri getirmek için SearchProductsUseCase sınıfı
class SearchProductsUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(query: String): List<Product> {
        return searchHistoryRepository.searchProducts(query)
    }

}