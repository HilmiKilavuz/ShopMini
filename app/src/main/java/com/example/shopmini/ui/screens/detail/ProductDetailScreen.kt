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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.shopmini.ui.components.ReviewCard
import com.example.shopmini.ui.screens.home.HomeUiState

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
    val uiState by viewModel.uiState.collectAsState()
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Button(onClick = onBackClick) { Text("Geri Dön") }
                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = product.thumbnail,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = product.title, style = MaterialTheme.typography.headlineMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "${product.price} $",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "-${product.discountPercentage.toInt()}%",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }

                }




                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description)

                //Yorumlar Bölümü
                Spacer(modifier = Modifier.height(16.dp))
                if (product.reviews == null) {
                    Text(
                        text = "Yorumlar Bulunamadı. İnternet Bağlantınızı Kontrol Edin.",
                        color = Color.Gray,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )

                }else{
                    Text(text = "Kullanıcı Yorumları", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    product.reviews.forEach { review ->
                        ReviewCard(review = review)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                //Sepete ekle ve Favorilere ekle butonları
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { /* Şimdilik boş */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Sepete Ekle")
                    }
                    Button(
                        onClick = { /* Şimdilik boş */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Favorilere Ekle")
                    }
                }


            }
        }

    }


}