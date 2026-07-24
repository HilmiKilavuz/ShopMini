/**
 * DATA Katmanı .
 * Dış dünya ile iletişim kurduğumuz (Retrofit) API arayüzüdür.
 * DummyJSON endpoint'lerine buradan istek atılır.
 */
package com.example.shopmini.data.remote

import com.example.shopmini.data.model.CategoryDto
import com.example.shopmini.data.model.Product
import com.example.shopmini.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface  ShopMiniApi {
    /**
     * Tüm ürünleri sayfalı olarak  getirir.
     * İstek atılan adres: GET /products?limit={limit}&skip={skip}
     */
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductResponse


    /**
     * Tüm kategorilerin listesini getirir.
     * İstek atılan adres: GET /products/categories
     */
    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>

    /**
     * İstenilen kategoriye (slug) ait ürünleri sayfalı olarak getirir.
     * İstek atılan adres: GET /products/category/{categorySlug}?limit={limit}&skip={skip}
     */
    @GET("products/category/{categorySlug}")
    suspend fun getProductsByCategory(
        @Path("categorySlug") categorySlug: String,
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductResponse

    /**
     * Belirli bir ürünün detaylarını getirir.
     * İstek atılan adres: GET /products/{id}
     */
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): ProductResponse
}
