package com.example.shopmini.ui.screens.checkout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shopmini.ui.components.checkout.CheckoutContent
import com.example.shopmini.ui.theme.Teal40

/**
 * Ödeme sürecinin ilk adımı olan teslimat adresi seçim ekranı.
 *
 * Bu ekranda kullanıcı:
 * - Varsayılan adresini görür (en üstte, seçili olarak)
 * - Açılır menüyle kayıtlı diğer adresleri seçebilir
 * - "Adres Ekle / Düzenle" butonu ile adres yönetimi ekranına gidebilir
 *
 * @param viewModel Hilt tarafından enjekte edilen, adres verilerini ve seçim mantığını yöneten ViewModel
 * @param onNavigateBack Geri tuşuna basıldığında çalışan fonksiyon (sepete döner)
 * @param onNavigateToAddresses Adres ekleme/düzenleme ekranına yönlendiren fonksiyon
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToPayment:()->Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadAddresses()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Teslimat Adresi",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
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
            )
        }
    ) { paddingValues ->

        when {
            // --- Yükleniyor durumu ---
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Teal40)
                }
            }

            // --- Hata durumu ---
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error ?: "Bir hata oluştu.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // --- Adres yok durumu ---
            uiState.addresses.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Teal40,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Henüz kayıtlı adresiniz yok.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onNavigateToAddresses) {
                            Text(text = "Adres Ekle →", color = Teal40, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // --- Başarılı durum: adresler yüklendi ---
            else -> {
                CheckoutContent(
                    uiState = uiState,
                    onAddressSelected = viewModel::onAddressSelected,
                    onNavigateToAddresses = onNavigateToAddresses,
                    modifier = Modifier.padding(paddingValues),
                    onNavigateToPayment = onNavigateToPayment
                )
            }
        }
    }
}








