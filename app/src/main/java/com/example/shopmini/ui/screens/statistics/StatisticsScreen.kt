package com.example.shopmini.ui.screens.statistics

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shopmini.ui.theme.Teal40
import com.example.shopmini.ui.theme.TealGrey40
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "İstatistiklerim",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
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
        when {
            uiState.isLoading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Teal40)
                }
            }

            uiState.error != null -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(4.dp)) }

                    // Özet Kartlar
                    item { SummarySection(uiState) }

                    // Aylık Harcama Grafiği
                    item { MonthlySpendingChart(uiState.monthlySpending) }

                    // Aylık Sipariş Grafiği
                    item { MonthlyOrderChart(uiState.monthlyOrderCount) }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

// ──────────────────────────────────────────────
// ÖZET KARTLAR
// ──────────────────────────────────────────────

@Composable
fun SummarySection(uiState: StatisticsUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatSummaryCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Star,
            iconBackground = Teal40,
            label = "Toplam Harcama",
            value = "${"%.2f".format(uiState.totalSpent)} ₺"
        )
        StatSummaryCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.ShoppingCart,
            iconBackground = TealGrey40,
            label = "Toplam Sipariş",
            value = "${uiState.totalOrders}"
        )
    }
}

@Composable
fun StatSummaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBackground: Color,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBackground.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconBackground,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ──────────────────────────────────────────────
// AYLIK HARCAMA GRAFİĞİ
// ──────────────────────────────────────────────

@Composable
fun MonthlySpendingChart(monthlySpending: Map<String, Double>) {
    if (monthlySpending.isEmpty()) return

    val sortedEntries = monthlySpending.entries.sortedBy { it.key }

    val chartEntryModel = entryModelOf(
        sortedEntries.mapIndexed { index, entry ->
            entryOf(index.toFloat(), entry.value.toFloat())
        }
    )

    // Teal renkli özel sütunlar
    val tealColumn = LineComponent(
        color = Teal40.toArgb(),
        thicknessDp = 16f,
        shape = Shapes.roundedCornerShape(topLeftPercent = 30, topRightPercent = 30)
    )

    ChartCard(title = "Aylık Harcama", subtitle = "Türk Lirası (₺)") {
        Chart(
            chart = columnChart(
                columns = listOf(tealColumn),
                mergeMode = ColumnChart.MergeMode.Grouped
            ),
            model = chartEntryModel,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            bottomAxis = rememberBottomAxis(
                label = textComponent {
                    color = android.graphics.Color.GRAY
                    textSizeSp = 11f
                },
                valueFormatter = { value, _ ->
                    val key = sortedEntries.getOrNull(value.toInt())?.key ?: ""
                    val monthNumber = if (key.length >= 7) key.substring(5, 7).toIntOrNull() ?: 0 else 0
                    if (monthNumber in 1..12) monthNames[monthNumber - 1] else key
                }
            ),
            startAxis = rememberStartAxis(
                label = textComponent {
                    color = android.graphics.Color.GRAY
                    textSizeSp = 11f
                }
            )
        )
    }
}

// ──────────────────────────────────────────────
// AYLIK SİPARİŞ GRAFİĞİ
// ──────────────────────────────────────────────

@Composable
fun MonthlyOrderChart(monthlyOrderCount: Map<String, Int>) {
    if (monthlyOrderCount.isEmpty()) return

    val sortedEntries = monthlyOrderCount.entries.sortedBy { it.key }

    val chartEntryModel = entryModelOf(
        sortedEntries.mapIndexed { index, entry ->
            entryOf(index.toFloat(), entry.value.toFloat())
        }
    )

    val tealGreyColumn = LineComponent(
        color = TealGrey40.toArgb(),
        thicknessDp = 16f,
        shape = Shapes.roundedCornerShape(topLeftPercent = 30, topRightPercent = 30)
    )

    ChartCard(title = "Aylık Sipariş", subtitle = "Adet") {
        Chart(
            chart = columnChart(
                columns = listOf(tealGreyColumn),
                mergeMode = ColumnChart.MergeMode.Grouped
            ),
            model = chartEntryModel,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            bottomAxis = rememberBottomAxis(
                label = textComponent {
                    color = android.graphics.Color.GRAY
                    textSizeSp = 11f
                },
                valueFormatter = { value, _ ->
                    val key = sortedEntries.getOrNull(value.toInt())?.key ?: ""
                    val monthNumber = if (key.length >= 7) key.substring(5, 7).toIntOrNull() ?: 0 else 0
                    if (monthNumber in 1..12) monthNames[monthNumber - 1] else key
                }
            ),
            startAxis = rememberStartAxis(
                label = textComponent {
                    color = android.graphics.Color.GRAY
                    textSizeSp = 11f
                },
                valueFormatter = { value, _ ->
                    // Sadece tam sayıları göster (1, 2, 3...) — kesirli değerleri gizle
                    if (value == value.toInt().toFloat()) value.toInt().toString() else ""
                }
            )
        )
    }
}

// ──────────────────────────────────────────────
// YARDIMCI COMPOSABLE'LAR
// ──────────────────────────────────────────────

@Composable
fun ChartCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

private val monthNames = listOf(
    "Oca", "Şub", "Mar", "Nis", "May", "Haz",
    "Tem", "Ağu", "Eyl", "Eki", "Kas", "Ara"
)
