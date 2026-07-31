package com.example.shopmini.ui.screens.home

/**
 * UI Katmanı (Salon).
 * Kullanıcının ürünleri gördüğü ana sayfadır.
 * Veri çekmez, sadece ViewModel'dan gelen State'i dinler ve Compose ile çizer.
 */


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopmini.ui.components.home.ProductGridWithFab
import com.example.shopmini.ui.components.home.SearchBar

//Anasayfa ekranını oluşturan composable fonksiyon
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit,

    ) {

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isloadingNextPage by viewModel.isLoadingNextPage.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {

        // Arama kutusu
        SearchBar(
            searchQuery = searchQuery,
            onQueryChange = { viewModel.onSearchQueryChanged(it) },
            onFocusChanged = { it -> isSearchFocused = it },
            onSearch = {
                viewModel.saveSearchQuery(searchQuery)
                focusManager.clearFocus()
            }
        )

        //Filtreleme bölümü
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.onCategorySelected(null) },
                    label = { Text("Tümü") }
                )
            }

            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category.slug,
                    onClick = { viewModel.onCategorySelected(category.slug) },
                    label = { Text(category.name) }
                )
            }
        }
//Veriler alınıp alınmadığına göre yapılacak için ayrılmış bölümler
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Success -> {
                val products = (uiState as HomeUiState.Success).products
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        viewModel.loadProducts()
                        viewModel.loadCategories()
                    }) {

                    ProductGridWithFab(
                        products = products,
                        isSearchFocused = isSearchFocused,
                        searchQuery = searchQuery,
                        searchHistory = searchHistory,
                        isloadingNextPage = isloadingNextPage,
                        onProductClick = onProductClick,
                        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                        onDeleteSearchHistory = { viewModel.deleteSearchHistory(it) },
                        onLoadNextPage = { viewModel.loadPage() }
                    )


                }


            }

            is HomeUiState.Error -> {
                val message = (uiState as HomeUiState.Error).message
                // Hata mesajı ve "Tekrar Dene" butonu
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Hata: $message")
                    Button(onClick = { viewModel.loadProducts() }) {
                        Text("Tekrar Dene")
                    }
                }
            }
        }

    }

}


