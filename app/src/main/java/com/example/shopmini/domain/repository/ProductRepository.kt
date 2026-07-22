package com.example.shopmini.domain.repository

import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.data.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getProductsByCategory(slug: String): List<Product>
}