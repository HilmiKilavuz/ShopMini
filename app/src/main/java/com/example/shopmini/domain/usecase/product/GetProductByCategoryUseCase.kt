package com.example.shopmini.domain.usecase.product

import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.repository.ProductRepository
import javax.inject.Inject
//Kategoriye göre ürünleri getirmek için GetProductByCategoryUseCase sınıfı
class GetProductByCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
)
{
    suspend operator fun invoke(slug: String,limit: Int = 20, skip: Int = 0): List<Product> {
       return productRepository.getProductsByCategory(slug,limit, skip)

    }
}