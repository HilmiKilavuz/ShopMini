package com.example.shopmini.domain.usecase.product

import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.repository.ProductRepository
import javax.inject.Inject
//Ürünleri getirmek için GetProductsUseCase sınıfı
class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(limit: Int = 20, skip: Int = 0): List<Product> {
        return productRepository.getProducts(limit, skip)
    }
}

