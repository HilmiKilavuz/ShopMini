/**
 * DATA Katmanı .
 * Cihazın içindeki yerel veritabanı (Room) yapılandırmasıdır.
 */
package com.example.shopmini.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class],
    version = 3)

abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao


}
