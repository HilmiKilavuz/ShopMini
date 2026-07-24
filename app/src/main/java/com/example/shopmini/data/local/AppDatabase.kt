/**
 * DATA Katmanı .
 * Cihazın içindeki yerel veritabanı (Room) yapılandırmasıdır.
 */
package com.example.shopmini.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class,SearchHistoryEntity::class],
    version = 4)

abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun searchHistoryDao(): SearchHistoryDao

}
