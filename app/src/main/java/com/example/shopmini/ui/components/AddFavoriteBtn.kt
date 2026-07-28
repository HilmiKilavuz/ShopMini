package com.example.shopmini.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.function.IntConsumer
//Favorilere ekleme butonu
@Composable
fun AddFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit
){

    IconButton(
        onClick =  onClick
    ) {
        Icon(
            imageVector = if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorilere Ekle",
            tint = if (isFavorite) Color.Red else Color.Gray
        )
    }
}
