package com.example.shopmini.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shopmini.data.local.SearchHistoryEntity

//Arama geçmişini göstermek için oluşturulmuş UI bileşenidir.
@Composable
fun SearchHistoryList(
    searchHistory: List<SearchHistoryEntity>,
    onHistoryItemClick: (String) -> Unit,
    onDeleteClick: (SearchHistoryEntity) -> Unit
) {
    Column(modifier = Modifier.padding(12.dp)) {
        Text("Son Aramalar", style = MaterialTheme.typography.titleSmall)

        searchHistory.forEach { historyItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHistoryItemClick(historyItem.query) }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(historyItem.query)
                IconButton(onClick = { onDeleteClick(historyItem) }) {
                    Icon(Icons.Default.Close, contentDescription = "Sil")
                }
            }
        }
    }
}