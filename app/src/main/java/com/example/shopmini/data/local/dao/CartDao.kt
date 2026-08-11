package com.example.shopmini.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

//sepet için veritabanı işlemleri
@Dao
interface CartDao {
    //Sepeti getirir
    @Query("Select * from cart")
    fun getCartItems(): Flow<List<CartEntity>>

    //Sepete ekler
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(cart: CartEntity)

    //Sepetten siler
    @Delete
    suspend fun deleteItem(cart: CartEntity)
    //Sepetten miktarı günceller
    @Query("UPDATE cart SET quantity = :newQuantity WHERE id = :productId")
    suspend fun updateQuantity(productId: Int, newQuantity: Int)
    //
    @Query("DELETE FROM cart")
    suspend fun deleteAllItems()




}