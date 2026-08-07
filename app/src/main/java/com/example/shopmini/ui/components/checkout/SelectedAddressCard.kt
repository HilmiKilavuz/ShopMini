package com.example.shopmini.ui.components.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopmini.domain.model.Address
import com.example.shopmini.ui.theme.Teal40
import com.example.shopmini.ui.theme.TealGrey40

/**
 * Seçili adresi gösteren kart. Teal sol şerit, "Varsayılan" chip'i ve onay ikonu içerir.
 */
@Composable
fun SelectedAddressCard(address: Address) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Sol teal aksent şeridi
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Teal40, TealGrey40)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = address.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // "Varsayılan" chip
                    if (address.isDefault) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Teal40.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Varsayılan",
                                color = Teal40,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "${address.fullName}  •  ${address.phone}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "${address.neighborhood}, ${address.district} / ${address.city}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = address.fullAddress,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                )
            }

            // Sağ taraf: Seçili onay ikonu
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Seçili",
                tint = Teal40,
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .size(22.dp)
            )
        }
    }
}