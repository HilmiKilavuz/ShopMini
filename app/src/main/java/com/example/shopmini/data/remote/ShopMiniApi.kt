package com.example.shopmini.data.remote

import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface ShopMiniApi {
    @GET("products")
    suspend fun getProducts(): ProductResponse

    // Tüm kategorileri getir
    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>

    // Seçili kategorinin ürünlerini getir
    @GET("products/category/{categorySlug}")
    suspend fun getProductsByCategory(
        @Path("categorySlug") categorySlug: String
    ): ProductResponse

}