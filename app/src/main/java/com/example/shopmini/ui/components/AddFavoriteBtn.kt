package com.example.shopmini.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.function.IntConsumer
//Favorilere ekleme butonu
@Composable
fun AddFavoriteButton(){

    IconButton(
        onClick = { /* Şimdilik boş */ }
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favorilere Ekle",
            tint = Color.Red
        )
    }
}
