package com.example.shopmini.domain.usecase.searchHistory

import com.example.shopmini.data.local.entity.SearchHistoryEntity
import com.example.shopmini.domain.repository.SearchHistoryRepository
import javax.inject.Inject
//Arama geçmişini silmek için DeleteSearcHistoryUseCase sınıfı
class DeleteSearcHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
){
    suspend operator fun invoke(entity: SearchHistoryEntity){
        searchHistoryRepository.deleteSearch(entity)
    }

}