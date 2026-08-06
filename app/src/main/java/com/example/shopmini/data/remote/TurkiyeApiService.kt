package com.example.shopmini.data.remote

import com.example.shopmini.data.model.address.DistrictsResponse
import com.example.shopmini.data.model.address.ProvincesResponse
import retrofit2.http.GET
import retrofit2.http.Path
//Retrofit arayüzünü tanımlayan interface(Türkiye il bilgilerini çeken arayüz)
interface TurkiyeApiService {
    @GET("v2/provinces")
    suspend fun getProvinces(): ProvincesResponse

    @GET("v2/provinces/{provinceId}/districts")
    suspend fun getDistricts(
        @Path("provinceId") provinceId: Int
    ): DistrictsResponse
}