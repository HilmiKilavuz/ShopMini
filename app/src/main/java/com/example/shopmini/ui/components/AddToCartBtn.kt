package com.example.shopmini.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


//Sepete ekleme butonu
@Composable
fun AddToCartBtn(modifier: Modifier) {
    Button(
        onClick = { /* Şimdilik boş */ },
        modifier = modifier
    ) {
        Text("Sepete Ekle")
    }
}