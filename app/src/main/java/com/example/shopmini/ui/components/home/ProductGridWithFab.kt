package com.example.shopmini.ui.components.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shopmini.data.local.entity.SearchHistoryEntity
import com.example.shopmini.data.model.Product
import kotlinx.coroutines.launch
//Ürünlerin bulunduğu ızgaralı yapıya göre listeler
@Composable
fun ProductGridWithFab(
    products: List<Product>,
    isSearchFocused: Boolean,
    searchQuery: String,
    searchHistory: List<SearchHistoryEntity>,
    isloadingNextPage: Boolean,
    onProductClick: (Int) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onDeleteSearchHistory: (SearchHistoryEntity) -> Unit,
    onLoadNextPage: () -> Unit
) {
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Sadece arama kutusu boşken geçmişi göster
            if (isSearchFocused && searchQuery.isBlank() && searchHistory.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SearchHistoryList(
                        searchHistory,
                        onHistoryItemClick = onSearchQueryChanged,
                        onDeleteClick = onDeleteSearchHistory
                    )
                }
            }

            itemsIndexed(products) { index, product ->
                if (index == products.lastIndex) {
                    LaunchedEffect(Unit) {
                        onLoadNextPage()
                    }
                }
                ProductCard(product, onProductClick)
            }
            if (isloadingNextPage) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }


        AnimatedVisibility(
            visible = gridState.firstVisibleItemIndex > 0,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        gridState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Yukarı Çık"
                )
            }
        }
    }
}


