package com.example.shopmini.ui.screens.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopmini.ui.components.cart.CartItemRow
import com.example.shopmini.ui.components.cart.CartSummaryBar

//Sepet ekranı

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onCheckoutClick: () -> Unit,

    ) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {

        }
    ) { padding ->

        if (uiState.cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sepetiniz Boş",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }
        } else {
            Column {
                // Ürün listesi
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(uiState.cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { viewModel.increaseQuantity(item) },
                            onDecrease = { viewModel.decreaseQuantity(item) },
                            onDelete = { viewModel.deleteCartItem(item) },
                            itemClick = { onItemClick(item.id) }
                        )
                    }
                }
                // Alt fiyat özeti
                CartSummaryBar(uiState = uiState, onCheckoutClick = {
                    viewModel.logBeginCheckout()
                    onCheckoutClick()
                })
            }
        }
    }
}


