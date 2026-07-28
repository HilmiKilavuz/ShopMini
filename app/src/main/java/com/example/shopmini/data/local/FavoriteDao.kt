package com.example.shopmini.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {
    //Favorileri getirir
    @Query("Select * from favorites")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    //Favorileri ekler
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    //Favoriler siler
    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    //Ürün favorilere eklenmiş mi kontrol eder
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :productId)")
    fun isFavorite(productId: Int): Flow<Boolean>

}