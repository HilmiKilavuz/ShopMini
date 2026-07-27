package com.example.shopmini.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//arama geçmişini tutuyoruz
@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true )val id :Int,
    val query: String,
    val timestamp: Long
)
