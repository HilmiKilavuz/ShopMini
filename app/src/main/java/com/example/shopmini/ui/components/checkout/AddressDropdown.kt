package com.example.shopmini.ui.components.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopmini.domain.model.Address
import com.example.shopmini.ui.theme.Teal40
import kotlin.collections.forEach

/**
 * Diğer adresleri listeleyen açılır / kapanır dropdown bileşeni.
 *
 * Seçili olmayan adresleri satır satır listeler. Her satır tıklanınca adres seçilir
 * ve dropdown kapanır.
 */
@Composable
fun AddressDropdown(
    addresses: List<Address>,
    selectedAddress: Address?,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onAddressSelected: (Address) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isExpanded) 1.5.dp else 1.dp,
                color = if (isExpanded) Teal40 else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Dropdown başlığı / seçili alan
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = selectedAddress?.title ?: "Adres Seçin",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (selectedAddress != null) {
                    Text(
                        text = "${selectedAddress.district} / ${selectedAddress.city}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )
                }
            }
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Kapat" else "Aç",
                tint = if (isExpanded) Teal40 else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Açılır liste
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                addresses.forEach { address ->
                    val isSelected = address.id == selectedAddress?.id

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddressSelected(address) }
                            .background(
                                if (isSelected) Teal40.copy(alpha = 0.06f)
                                else Color.Transparent
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onAddressSelected(address) },
                            colors = RadioButtonDefaults.colors(selectedColor = Teal40)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = address.title,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (address.isDefault) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Teal40.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = "Varsayılan",
                                            color = Teal40,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${address.district} / ${address.city}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                            )
                        }
                    }

                    if (address != addresses.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                        )
                    }
                }
            }
        }
    }
}