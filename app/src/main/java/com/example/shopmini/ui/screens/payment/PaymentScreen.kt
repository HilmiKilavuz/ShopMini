package com.example.shopmini.ui.screens.payment

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.shopmini.ui.theme.Teal40
import com.example.shopmini.ui.theme.TealGrey40

/**
 * Sadece rakam içeren expiryDate state'ini görünümde "MM/YY" formatında gösterir.
 * İçeride state her zaman temiz rakamları (örn. "1231") tutar.
 */
private class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(4)
        val out = if (digits.length >= 3) "${digits.take(2)}/${digits.drop(2)}" else digits
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                if (offset <= 2) offset else offset + 1 // "/" için +1
            override fun transformedToOriginal(offset: Int): Int =
                if (offset <= 2) offset else (offset - 1).coerceAtLeast(0)
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

//Ödeme Ekranı Tasarımı
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* izin verildi/reddedildi — şimdilik log yeterli */ }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // isPaymentSuccessful true olunca otomatik geç
    LaunchedEffect(uiState.isPaymentSuccessful) {
        if (uiState.isPaymentSuccessful) onNavigateToSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ödeme Sayfası",
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
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Scrollable içerik
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.cardNumber,
                    onValueChange = viewModel::onCardNumberChange,
                    label = { Text("Kart Numarası") },
                    placeholder = { Text("1234 5678 9012 3456") },
                    isError = uiState.cardNumberError != null,
                    supportingText = {
                        uiState.cardNumberError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Teal40,
                        focusedLabelColor = Teal40,
                        cursorColor = Teal40
                    )
                )
                OutlinedTextField(
                    value = uiState.cardHolderName,
                    onValueChange = viewModel::onCardHolderNameChange,
                    label = { Text("Kart Sahibi") },
                    placeholder = { Text("İSİM SOYAD") },
                    isError = uiState.cardHolderNameError != null,
                    supportingText = {
                        uiState.cardHolderNameError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Teal40,
                        focusedLabelColor = Teal40,
                        cursorColor = Teal40
                    )
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.expiryDate,
                        onValueChange = viewModel::onExpiryDateChange,
                        label = { Text("Son Kullanma Tarihi") },
                        placeholder = { Text("MM/YY") },
                        isError = uiState.expiryDateError != null,
                        supportingText = {
                            uiState.expiryDateError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = ExpiryDateVisualTransformation(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Teal40,
                            focusedLabelColor = Teal40,
                            cursorColor = Teal40
                        )
                    )
                    OutlinedTextField(
                        value = uiState.cvv,
                        onValueChange = viewModel::onCvvChange,
                        label = { Text("CVV") },
                        placeholder = { Text("123") },
                        isError = uiState.cvvError != null,
                        supportingText = {
                            uiState.cvvError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Teal40,
                            focusedLabelColor = Teal40,
                            cursorColor = Teal40
                        )
                    )
                }
                CardPreview(
                    cardNumber = uiState.cardNumber,
                    cardHolderName = uiState.cardHolderName,
                    // Ham rakamları "MM/YY" formatında göster
                    expiryDate = uiState.expiryDate.let { d ->
                        if (d.length >= 3) "${d.take(2)}/${d.drop(2)}" else d
                    }
                )

                // ── İndirim Kuponu Bölümü ──
                CouponSection(
                    couponCode = uiState.couponCode,
                    couponError = uiState.couponError,
                    isValidating = uiState.isValidatingCoupon,
                    appliedCouponCode = uiState.appliedCoupon?.code,
                    appliedDiscountPercent = uiState.appliedCoupon?.discountPercent,
                    onCouponCodeChange = viewModel::onCouponCodeChange,
                    onApplyCoupon = viewModel::onApplyCoupon,
                    onRemoveCoupon = viewModel::onRemoveCoupon
                )
            }

            // Sabit alt özet çubuğu
            PaymentSummaryBar(
                isLoading = uiState.isLoading,
                subtotal = uiState.subtotal,
                productDiscount = uiState.productDiscount,
                couponDiscountAmount = uiState.couponDiscountAmount,
                appliedCouponCode = uiState.appliedCoupon?.code,
                finalAmount = uiState.finalAmount,
                onPayClicked = viewModel::onPayClicked
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Kupon bölümü
// ──────────────────────────────────────────────────────────────

@Composable
private fun CouponSection(
    couponCode: String,
    couponError: String?,
    isValidating: Boolean,
    appliedCouponCode: String?,
    appliedDiscountPercent: Int?,
    onCouponCodeChange: (String) -> Unit,
    onApplyCoupon: () -> Unit,
    onRemoveCoupon: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Başlık
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = Teal40,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "İndirim Kuponu",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Kupon uygulanmadıysa giriş alanını göster
            AnimatedVisibility(visible = appliedCouponCode == null, enter = fadeIn(), exit = fadeOut()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    OutlinedTextField(
                        value = couponCode,
                        onValueChange = onCouponCodeChange,
                        label = { Text("Kupon Kodu") },
                        placeholder = { Text("Örn: SHOPOFF10") },
                        isError = couponError != null,
                        supportingText = {
                            couponError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Teal40,
                            focusedLabelColor = Teal40,
                            cursorColor = Teal40
                        )
                    )
                    Button(
                        onClick = onApplyCoupon,
                        enabled = !isValidating,
                        modifier = Modifier
                            .height(56.dp)
                            .align(Alignment.CenterVertically),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Teal40)
                    ) {
                        if (isValidating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Uygula", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Kupon uygulandıysa başarı durumunu göster
            AnimatedVisibility(visible = appliedCouponCode != null, enter = fadeIn(), exit = fadeOut()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Teal40,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = appliedCouponCode ?: "",
                                fontWeight = FontWeight.Bold,
                                color = Teal40,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "%${appliedDiscountPercent} indirim uygulandı!",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    TextButton(onClick = onRemoveCoupon) {
                        Text(
                            text = "Kaldır",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Ödeme özet çubuğu
// ──────────────────────────────────────────────────────────────

@Composable
private fun PaymentSummaryBar(
    isLoading: Boolean,
    subtotal: Double,
    productDiscount: Double,
    couponDiscountAmount: Double,
    appliedCouponCode: String?,
    finalAmount: Double,
    onPayClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Ara toplam
            SummaryRow(label = "Ara Toplam", amount = subtotal)

            // Ürün indirimi — sadece varsa göster
            if (productDiscount > 0) {
                SummaryRow(
                    label = "Ürün İndirimi",
                    amount = -productDiscount,
                    isDiscount = true
                )
            }

            // Kupon indirimi — sadece uygulandıysa göster
            if (couponDiscountAmount > 0 && appliedCouponCode != null) {
                SummaryRow(
                    label = "Kupon ($appliedCouponCode)",
                    amount = -couponDiscountAmount,
                    isDiscount = true
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Ödenecek — kalın ve büyük
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ödenecek",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "₺ %.2f".format(finalAmount),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Teal40
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Devam butonu — teal gradient
            Button(
                onClick = onPayClicked,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Teal40, TealGrey40)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "İşlemi Tamamla →",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/** Özet satırı yardımcısı */
@Composable
private fun SummaryRow(
    label: String,
    amount: Double,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = if (isDiscount) "-₺ %.2f".format(-amount) else "₺ %.2f".format(amount),
            fontSize = 14.sp,
            color = if (isDiscount) Teal40 else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isDiscount) FontWeight.Medium else FontWeight.Normal
        )
    }
}

// ──────────────────────────────────────────────────────────────
// Kart önizlemesi (değişmedi)
// ──────────────────────────────────────────────────────────────

@Composable
private fun CardPreview(
    cardNumber: String,
    cardHolderName: String,
    expiryDate: String
) {
    val displayNumber = cardNumber
        .filter { it.isDigit() }
        .chunked(4)
        .joinToString(" ")
        .let { it.padEnd(19, '•') }  // eksik yerleri doldur

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Teal40, TealGrey40)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("ShopMini", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)

            Text(
                text = displayNumber,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("KART SAHİBİ", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                    Text(
                        text = cardHolderName.uppercase().ifBlank { "AD SOYAD" },
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("SON KULLANMA", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                    Text(
                        text = expiryDate.ifBlank { "AA/YY" },
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
