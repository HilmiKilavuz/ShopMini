package com.example.shopmini.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
//sepet için tablo
@Entity(tableName ="cart" )
data class CartEntity (
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val quantity: Int,
    val thumbnail: String,
    val discountPercentage: Double,
    val stock: Int
)