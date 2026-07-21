package com.example.shopmini.domain.repository

import com.example.shopmini.data.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}