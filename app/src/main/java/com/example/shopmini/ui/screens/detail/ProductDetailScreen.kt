package com.example.shopmini.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.shopmini.ui.components.AddFavoriteButton
import com.example.shopmini.ui.components.AddToCartBtn
import com.example.shopmini.ui.components.CartQuantityControl
import com.example.shopmini.ui.components.ReviewCard

/**
 * UI Katmanı.
 * Ürünün tüm detaylarının gösterildiği ekrandır.
 * ViewModel'dan gelen State (Durum) bilgisine göre sayfayı çizer.
 */
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    // Kullanıcı geri butonuna bastığında NavHost'un popBackStack fonksiyonunu tetikleyecek
    onBackClick: () -> Unit
) {
    // ViewModel'daki anlık durumu dinliyoruz
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    when (uiState) {
        is ProductDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }

        is ProductDetailUiState.Error -> {
            val message = (uiState as ProductDetailUiState.Error).message
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Hata: $message")

            }
        }


        is ProductDetailUiState.Success -> {
            val product = (uiState as ProductDetailUiState.Success).product

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    viewModel.loadProductDetail(product.id)

                }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Button(onClick = onBackClick,
                        modifier = Modifier.wrapContentSize()) { Text("Geri Dön") }
                    Spacer(modifier = Modifier.height(16.dp))


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {


                        AsyncImage(
                            model = product.thumbnail,
                            contentDescription = product.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = product.title, style = MaterialTheme.typography.headlineMedium)

                        Text(text = product.description)

                        // Yorumlar Bölümü
                        Spacer(modifier = Modifier.height(16.dp))
                        if (product.reviews == null) {
                            Text(
                                text = "Kullanıcı Yorumları",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Yorumlar Bulunamadı. İnternet Bağlantınızı Kontrol Edin.",
                                color = Color.Gray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        } else {
                            Text(
                                text = "Kullanıcı Yorumları",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            product.reviews.forEach { review ->
                                ReviewCard(review = review)
                            }
                        }


                    }


                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val cartItem by viewModel.cartItem.collectAsState()


                        if (cartItem == null) {
                            // Sepette YOK → "Sepete Ekle" butonu göster
                            AddToCartBtn(
                                modifier = Modifier.weight(1f).height(50.dp),
                                addCart = { viewModel.insertCart(product) }
                            )
                        } else {
                            // Sepette VAR → adet kontrol paneli göster
                            CartQuantityControl(
                                quantity = cartItem!!.quantity,
                                onIncrease = { viewModel.increaseQuantity(cartItem!!) },
                                onDecrease = { viewModel.decreaseQuantity(cartItem!!) },
                                modifier = Modifier.weight(1f).height(50.dp)
                            )
                        }

                        AddFavoriteButton(isFavorite, onClick = {viewModel.toggleFavorite(product)})
                    }



                }
            }



        }
        }
    }


