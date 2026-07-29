package com.example.shopmini.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//Favoriler için tablo
@Entity(tableName ="favorites" )
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val discountPercentage: Double
)