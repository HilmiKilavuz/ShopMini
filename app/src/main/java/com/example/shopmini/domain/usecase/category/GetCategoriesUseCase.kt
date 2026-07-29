package com.example.shopmini.domain.usecase.category

import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.domain.repository.CategoryRepository
import javax.inject.Inject
//Kategorileri getirmek için GetCategoriesUseCase sınıfı
class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
){
    suspend operator fun invoke(): List<CategoryDto>{
       return categoryRepository.getCategories()
    }


}