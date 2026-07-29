package com.example.shopmini.domain.usecase.product

import com.example.shopmini.data.model.Product
import com.example.shopmini.domain.repository.ProductRepository
import javax.inject.Inject
//Ürün detaylarını getirmek için GetProductDetailUseCase sınıfı
class GetProductDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Product {
        return productRepository.getProductById(id)
    }
}
