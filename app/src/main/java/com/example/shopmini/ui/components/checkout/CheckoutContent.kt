package com.example.shopmini.ui.components.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopmini.domain.model.Address

import com.example.shopmini.ui.screens.checkout.CheckoutUiState

import com.example.shopmini.ui.theme.Teal40

/**
 * Adresler başarıyla yüklendiğinde gösterilen ana içerik.
 *
 * Üst kısım kaydırılabilir (scroll), alt kısım (sipariş özeti + buton) ekrana sabitlenmiştir.
 */
@Composable
fun CheckoutContent(
    uiState: CheckoutUiState,
    onAddressSelected: (Address) -> Unit,
    onNavigateToAddresses: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Açılır adres listesinin açık/kapalı durumu
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // --- Kaydırılabilir üst içerik ---
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // -- Bölüm başlığı: Seçili Adres --
            SectionLabel(title = "Seçili Adres")

            // -- Seçili adres kartı --
            uiState.selectedAddress?.let { selected ->
                SelectedAddressCard(address = selected)
            }

            // -- Bölüm başlığı: Farklı Adres Seç --
            if (uiState.addresses.size > 1) {
                SectionLabel(title = "Farklı Adres Seç")

                // -- Açılır adres seçici --
                AddressDropdown(
                    addresses = uiState.addresses,
                    selectedAddress = uiState.selectedAddress,
                    isExpanded = isDropdownExpanded,
                    onToggle = { isDropdownExpanded = !isDropdownExpanded },
                    onAddressSelected = { address ->
                        onAddressSelected(address)
                        isDropdownExpanded = false
                    }
                )
            }

            // -- Adres yönetimi linki --
            TextButton(
                onClick = onNavigateToAddresses,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Teal40,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Adres Ekle / Düzenle →",
                    color = Teal40,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }

        // --- Alt kısım: Sipariş özeti + Devam butonu (sabitlenmiş) ---
        CheckoutSummaryBar(
            onContinueClick = { /* ileride ödeme adımı eklenecek */ }
        )
    }
}
