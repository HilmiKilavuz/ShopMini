package com.example.shopmini.data.remote

import com.example.shopmini.data.model.ProductResponse
import retrofit2.http.GET


interface ShopMiniApi {
    @GET("products")
    suspend fun getProducts(): ProductResponse

}