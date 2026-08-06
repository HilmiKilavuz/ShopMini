package com.example.shopmini.ui.screens.profile.address

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shopmini.ui.components.address.AddressCard

/**
 * Kullanıcının eklediği tüm adreslerin listelendiği ekran (UI).
 * 
 * Bu ekran, [AddressesViewModel] içerisindeki `uiState` (StateFlow) değişkenini dinler.
 * Veri durumuna göre "Yükleniyor (Loading)", "Hata (Error)" veya "Başarılı (Success)" 
 * görünümlerinden birini ekrana çizer.
 *
 * @param viewModel Hilt tarafından otomatik enjekte edilen ve ekranın mantığını yöneten ViewModel
 * @param onNavigateToAddAddress Yeni adres ekleme sayfasına yönlendirme yapan geri arama fonksiyonu
 * @param onNavigateToEditAddress Var olan adresi düzenleme sayfasına yönlendiren fonksiyon (İlgili adresin ID'sini taşır)
 * @param onNavigateBack Bir önceki sayfaya (örneğin Profil sayfasına) dönmek için kullanılan geri arama fonksiyonu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressesScreen(
    viewModel: AddressesViewModel= hiltViewModel(),
    onNavigateToAddAddress: () -> Unit,
    onNavigateToEditAddress: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    // ViewModel'deki state'i Compose'a bağlıyoruz
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Ekrana her geri dönüldüğünde (veya açıldığında) listeyi yenile
    // Not: Başka bir ekrandan buraya geri (popBackStack ile) dönüldüğünde 
    // ViewModel'in init bloğu tekrar çalışmaz. Bu nedenle listeyi her girişte tazelemek
    // için LaunchedEffect içerisinde loadAddresses fonksiyonunu çağırıyoruz.
    LaunchedEffect(Unit) {
        viewModel.loadAddresses()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddAddress) {
                Icon(Icons.Default.Add, contentDescription = "Yeni Adres Ekle")
            }
        },
        topBar ={TopAppBar(
            title = {
                Text(text = "Adreslerim", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )}
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            when (val state = uiState) {
                is AddressesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is AddressesUiState.Error -> {
                    Text(text = state.message, modifier = Modifier.align(Alignment.Center))
                }

                is AddressesUiState.Success -> {
                    if (state.addresses.isEmpty()) {
                        Text(
                            text = "Henüz bir adres eklemediniz.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn {
                            items(state.addresses) { address ->
                                AddressCard(
                                    address = address,
                                    actionContent = {
                                        Row {
                                            IconButton(onClick = { onNavigateToEditAddress(address.id) }) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Düzenle"
                                                )
                                            }
                                            IconButton(onClick = { viewModel.deleteAddress(address.id) }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Sil"
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}