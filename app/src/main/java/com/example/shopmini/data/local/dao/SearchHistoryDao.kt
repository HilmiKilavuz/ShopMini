package com.example.shopmini.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopmini.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

//arama geçmişi tablosu için işlemler
@Dao
interface SearchHistoryDao {
//son 10 aramayı döndürüyor
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
     fun getRecentSearches(): Flow<List<SearchHistoryEntity>>

     //arama geçmişini ekliyor
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(entity: SearchHistoryEntity)

    //arama geçmişini temizliyor
    @Delete
    suspend fun deleteSearch(entity: SearchHistoryEntity)
}