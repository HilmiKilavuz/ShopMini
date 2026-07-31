package com.example.shopmini.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopmini.ui.components.favorite.FavoriteCard
//Favoriler için oluşturulmuş UI bileşenidir.
@Composable
fun FavoriteScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    when (uiState) {
        is FavoriteUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FavoriteUiState.Success -> {
            val favorites = (uiState as FavoriteUiState.Success).favorites

            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Favoriler",
                    style = MaterialTheme.typography.headlineMedium
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    items(favorites) { favorite ->
                        FavoriteCard(favorite,
                            deleteFavorite = {
                                viewModel.deleteFavorite(it)
                            },
                            onCardClick = {
                                onNavigateToDetail(favorite.id)
                            })
                    }

                }


            }


        }

        is FavoriteUiState.Error -> {
            val message = (uiState as FavoriteUiState.Error).message
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Hata: $message")

            }

        }
    }


}