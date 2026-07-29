package com.example.shopmini.domain.usecase.searchHistory

import com.example.shopmini.data.local.SearchHistoryEntity
import com.example.shopmini.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//Arama geçmişini getirmek için GetRecentSearchesUseCase sınıfı
class GetRecentSearchesUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {

     operator fun invoke(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryRepository.getRecentSearches()
    }


}