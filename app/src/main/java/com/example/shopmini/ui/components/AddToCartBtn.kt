package com.example.shopmini.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


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