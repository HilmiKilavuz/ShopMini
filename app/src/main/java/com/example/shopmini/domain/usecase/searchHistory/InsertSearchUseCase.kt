package com.example.shopmini.domain.usecase.searchHistory

import com.example.shopmini.data.local.SearchHistoryEntity
import com.example.shopmini.domain.repository.SearchHistoryRepository
import javax.inject.Inject
//Arama sorgusunu kaydetmek için InsertSearchUseCase sınıfı
class InsertSearchUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(entity: SearchHistoryEntity) {
        searchHistoryRepository.insertSearch(entity)
    }

}