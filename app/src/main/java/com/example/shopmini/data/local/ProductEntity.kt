/**
 * DATA Katmanı.
 * Room veritabanında saklanacak Tablonun şeklini belirler.
 */
package com.example.shopmini.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
//Product tablosu

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val discountPercentage: Double,
    val category:String

)
