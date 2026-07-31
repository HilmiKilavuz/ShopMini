package com.example.shopmini.domain.repository

import com.example.shopmini.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

//Sepete ürün ekleme işlemleri
interface CartRepository {
    //Sepeti getirir
    fun getCartItems(): Flow<List<CartEntity>>
    //Sepete ekler
    suspend fun insertItem(cart: CartEntity)
    //Sepetten siler
    suspend fun deleteItem(cart: CartEntity)
    //Sepetten miktarı günceller
    suspend fun updateQuantity(productId: Int, newQuantity: Int)


}