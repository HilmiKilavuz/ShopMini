package com.example.shopmini.data.remote

import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ShopMiniApi {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductResponse


    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("products/category/{categorySlug}")
    suspend fun getProductsByCategory(
        @Path("categorySlug") categorySlug: String,
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductResponse

}