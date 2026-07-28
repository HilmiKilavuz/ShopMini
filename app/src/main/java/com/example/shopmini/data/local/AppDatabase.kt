/**
 * DATA Katmanı .
 * Cihazın içindeki yerel veritabanı (Room) yapılandırmasıdır.
 */
package com.example.shopmini.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
//Veritabanın altında bulunan tabloları tanımladık
@Database(
    entities = [ProductEntity::class, SearchHistoryEntity::class, FavoriteEntity::class],
    version = 5
)
//Veritabanın fonksiyonlarını tanımladık
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun favoriteDao(): FavoriteDao

}
