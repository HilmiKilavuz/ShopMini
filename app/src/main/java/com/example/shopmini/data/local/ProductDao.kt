/**
 * DATA Katmanı.
 * Room veritabanına sorgu (SQL) attığımız DAO  arayüzüdür.
 */
package com.example.shopmini.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ProductDao {
    /**
     * Veritabanında kayıtlı tüm ürünleri getirir.
     */
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>

    /**
     * API'den gelen ürünleri veritabanına kaydeder.
     * Eğer aynı ID'ye sahip ürün varsa, üzerine yazar .
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts( products: List<ProductEntity>)

    /**
     * Veritabanındaki tüm ürünleri siler.
     */
    @Query("DELETE FROM products")
    suspend fun clearProducts()

    /**
     * Veritabanındaki istenilen ürünü getirir.
     */
    @Query("Select * from products where id = :id")
    suspend fun getProductById(id: Int): ProductEntity?
}
