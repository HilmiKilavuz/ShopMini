/**
 * DATA Katmanı .
 * Cihazın içindeki yerel veritabanı (Room) yapılandırmasıdır.
 */
package com.example.shopmini.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shopmini.data.local.dao.CartDao
import com.example.shopmini.data.local.dao.FavoriteDao
import com.example.shopmini.data.local.dao.ProductDao
import com.example.shopmini.data.local.dao.SearchHistoryDao
import com.example.shopmini.data.local.entity.CartEntity
import com.example.shopmini.data.local.entity.FavoriteEntity
import com.example.shopmini.data.local.entity.ProductEntity
import com.example.shopmini.data.local.entity.SearchHistoryEntity

//Veritabanın altında bulunan tabloları tanımladık
@Database(
    entities = [ProductEntity::class, SearchHistoryEntity::class, FavoriteEntity::class, CartEntity::class],
    version = 7
)
//Veritabanın fonksiyonlarını tanımladık
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun favoriteDao(): FavoriteDao

    abstract fun cartDao(): CartDao

}
