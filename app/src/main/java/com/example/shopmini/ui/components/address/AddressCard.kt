package com.example.shopmini.ui.components.address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shopmini.domain.model.Address

@Composable
fun AddressCard(
    address: Address,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}, // Kartın üstüne tıklanma durumu
    actionContent: @Composable () -> Unit = {} // Sağ tarafa eklenecek butonlar (Sil, Düzenle veya RadioButton)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = address.title, fontWeight = FontWeight.Bold)
                Text(text = "${address.fullName} - ${address.phone}")
                Text(text = "${address.neighborhood}, ${address.district}/${address.city}")
                Text(text = address.fullAddress)
            }

            // Dışarıdan vereceğimiz aksiyonlar burada gözükecek
            Box {
                actionContent()
            }
        }
    }
}