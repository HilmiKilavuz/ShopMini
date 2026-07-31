package com.example.shopmini.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * DATA Katmanı.
 * Room veritabanında saklanacak Tablonun şeklini belirler.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val discountPercentage: Double,
    val category: String,
    val stock: Int
)