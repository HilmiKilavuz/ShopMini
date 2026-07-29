package com.example.shopmini.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

//Arama çubuğu için oluşturulmuş UI bileşenidir.
@Composable
fun SearchBar(searchQuery: String,
              onQueryChange: (String) -> Unit,
              onFocusChanged: (Boolean) -> Unit,
              onSearch: () -> Unit
){
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { onQueryChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .onFocusEvent(){focusState ->
                onFocusChanged(focusState.isFocused)
            },
        placeholder = { Text("Ürün ara...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("")}) {
                    Icon(Icons.Default.Clear, contentDescription = "Temizle")
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch()
        })
    )

}
