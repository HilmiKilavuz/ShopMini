package com.example.shopmini.ui.screens.profile.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Kullanıcının yeni bir adres eklemesini veya mevcut bir adresi düzenlemesini sağlayan Form ekranı.
 * 
 * Bu ekran, [AddEditAddressViewModel] içindeki değişkenlerle iki yönlü (two-way binding) olarak çalışır.
 * Kullanıcı klavyeden bir şeyler yazdıkça ViewModel'deki ilgili state (ör: `title`, `fullName`) 
 * anında güncellenir.
 * 
 * Ekran ilk açıldığında `uiState` "Loading" ise ortada dönen bir yüklenme ikonu gösterir.
 * Bu genellikle "Adresi Düzenle" butonuna tıklandığında eski verilerin veritabanından 
 * getirilmesini beklerken kullanılır. Veriler inip `Success` state'ine geçildiğinde form çizilir.
 *
 * @param viewModel Hilt tarafından sağlanan ve formun iş mantığını yöneten ViewModel
 * @param onNavigateBack İşlem (kayıt veya güncelleme) bittiğinde veya geri butonuna basıldığında
 *                       çalışacak geri dönme fonksiyonu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAddressScreen(
    viewModel: AddEditAddressViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.isSaved.collect { saved ->
            if (saved) onNavigateBack()
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is AddEditAddressUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AddEditAddressUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is AddEditAddressUiState.Success->{
            var cityDropdownExpanded by remember { mutableStateOf(false) }
            var districtDropdownExpanded by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = if (viewModel.isEditing) "Adresi Düzenle" else "Yeni Adres Ekle",
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // --- BÖLÜM 1: GENEL BİLGİLER ---
                    SectionCard(title = "Genel Bilgiler", icon = Icons.Default.Info) {
                        AddressTextField(
                            value = viewModel.title,
                            onValueChange = { viewModel.title = it },
                            label = "Adres Başlığı",
                            placeholder = "Örn: Ev, İş, Anne Evi",
                            leadingIcon = Icons.Default.Label
                        )

                        Text(
                            text = "Adres Tipi",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AddressTypeChip(
                                label = "Ev", icon = Icons.Default.Home,
                                selected = viewModel.addressType == "Ev",
                                onClick = { viewModel.addressType = "Ev" }, modifier = Modifier.weight(1f)
                            )
                            AddressTypeChip(
                                label = "İş", icon = Icons.Default.Business,
                                selected = viewModel.addressType == "İş",
                                onClick = { viewModel.addressType = "İş" }, modifier = Modifier.weight(1f)
                            )
                            AddressTypeChip(
                                label = "Diğer",
                                icon = Icons.Default.Place,
                                selected = viewModel.addressType == "Diğer",
                                onClick = { viewModel.addressType = "Diğer" },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // --- BÖLÜM 2: TESLİMAT BİLGİLERİ ---
                    SectionCard(title = "Teslimat Bilgileri", icon = Icons.Default.Person) {
                        AddressTextField(
                            value = viewModel.fullName,
                            onValueChange = { viewModel.fullName = it },
                            label = "Ad Soyad", placeholder = "Teslim alacak kişi",
                            leadingIcon = Icons.Default.Person
                        )
                        AddressTextField(
                            value = viewModel.phone,
                            onValueChange = { newValue ->
                                if (newValue.length <= 10 && newValue.all { it.isDigit() })
                                    viewModel.phone = newValue
                            },
                            label = "Telefon", placeholder = "5XX XXX XX XX",
                            leadingIcon = Icons.Default.Phone, keyboardType = KeyboardType.Phone,
                            prefix = { Text("+90 ", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        )
                    }

                    // --- BÖLÜM 3: ADRES DETAYLARI ---
                    SectionCard(title = "Adres Detayları", icon = Icons.Default.LocationOn) {

                        // İL Dropdown
                        ExposedDropdownMenuBox(
                            expanded = cityDropdownExpanded,
                            onExpandedChange = { cityDropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = viewModel.city, onValueChange = {}, readOnly = true,
                                label = { Text("İl") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationCity,
                                        null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                trailingIcon = {
                                    if (viewModel.isProvincesLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityDropdownExpanded)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp), colors = textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = cityDropdownExpanded,
                                onDismissRequest = { cityDropdownExpanded = false }
                            ) {
                                viewModel.provinces.forEach { province ->
                                    DropdownMenuItem(
                                        text = { Text(province.name) },
                                        onClick = {
                                            viewModel.onCitySelected(province)
                                            cityDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // İLÇE Dropdown (İl seçilmeden devre dışı)
                        ExposedDropdownMenuBox(
                            expanded = districtDropdownExpanded && viewModel.city.isNotEmpty(),
                            onExpandedChange = {
                                if (viewModel.city.isNotEmpty()) districtDropdownExpanded = it
                            }
                        ) {
                            OutlinedTextField(
                                value = viewModel.district, onValueChange = {}, readOnly = true,
                                label = { Text("İlçe") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Map, null,
                                        tint = if (viewModel.city.isNotEmpty())
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                    )
                                },
                                trailingIcon = {
                                    if (viewModel.isDistrictsLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = districtDropdownExpanded && viewModel.city.isNotEmpty()
                                        )
                                    }
                                },
                                enabled = viewModel.city.isNotEmpty(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp), colors = textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = districtDropdownExpanded && viewModel.city.isNotEmpty(),
                                onDismissRequest = { districtDropdownExpanded = false }
                            ) {
                                viewModel.districts.forEach { district ->
                                    DropdownMenuItem(
                                        text = { Text(district.name) },
                                        onClick = {
                                            viewModel.district = district.name
                                            districtDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Mahalle
                        AddressTextField(
                            value = viewModel.neighborhood, onValueChange = { viewModel.neighborhood = it },
                            label = "Mahalle / Semt", placeholder = "Mahalle veya semt adı",
                            leadingIcon = Icons.Default.Place
                        )

                        // Açık Adres
                        OutlinedTextField(
                            value = viewModel.fullAddress, onValueChange = { viewModel.fullAddress = it },
                            label = { Text("Açık Adres") },
                            placeholder = { Text("Cadde, sokak, bina no, daire no...") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Home,
                                    null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier.fillMaxWidth(), minLines = 3, maxLines = 5,
                            shape = RoundedCornerShape(12.dp), colors = textFieldColors()
                        )
                    }

                    // --- BÖLÜM 4: TERCİHLER ---
                    SectionCard(title = "Tercihler", icon = Icons.Default.Settings) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Varsayılan Adres Yap",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "Bu adresi her zaman varsayılan olarak kullan",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = viewModel.isDefault,
                                onCheckedChange = { viewModel.isDefault = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- KAYDET BUTONU ---
                    Button(
                        onClick = { viewModel.saveAddress() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        enabled = viewModel.isFormValid()
                    ) {
                        Icon(Icons.Default.Save, null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (viewModel.isEditing) "Adresi Güncelle" else "Adresi Kaydet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

        }

    }


}

// --- YARDIMCI COMPOSABLE'LAR ---

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    title, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            content()
        }
    }
}

@Composable
private fun AddressTextField(
    value: String, onValueChange: (String) -> Unit, label: String, placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier, keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true, prefix: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = {
            Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            Icon(
                leadingIcon, null,
                tint = if (enabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
        },
        prefix = prefix, modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType), enabled = enabled,
        colors = textFieldColors()
    )
}

@Composable
private fun AddressTypeChip(
    label: String, icon: ImageVector,
    selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected, onClick = onClick,
        label = { Text(label, fontSize = 13.sp) },
        leadingIcon = { Icon(icon, null, modifier = Modifier.size(16.dp)) },
        modifier = modifier, shape = RoundedCornerShape(10.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    cursorColor = MaterialTheme.colorScheme.primary
)
