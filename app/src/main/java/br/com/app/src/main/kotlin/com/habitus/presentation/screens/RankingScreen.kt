package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.RankingViewModel
import br.com.app.src.main.kotlin.com.habitus.sample.listOfCategories
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.AngleDownSolid
import compose.icons.lineawesomeicons.AngleLeftSolid
import compose.icons.lineawesomeicons.AngleRightSolid
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

import android.view.ViewGroup
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun RankingScreen(
    viewModel: RankingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val barChartState by viewModel.barChartState.collectAsState()

    LaunchedEffect(
        uiState.selectedDate,
        uiState.selectedRange,
        uiState.selectedCategory
    ) {
        viewModel.loadStats()
        viewModel.loadChartData(uiState.selectedRange, uiState.selectedDate)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        RankingTypeSelector(
            selected = uiState.selectedRange,
            onSelect = viewModel::onRangeChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateNavigator(
            range = uiState.selectedRange,
            currentDate = uiState.selectedDate,
            onDateChange = viewModel::onDateChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        StatsCard(
            selectedCategoria = uiState.selectedCategory,
            onCategoriaChange = viewModel::onCategoryChange,
            taxaSucesso = uiState.successRate,
            pontos = uiState.points,
            completados = uiState.completed,
            pulados = uiState.skipped
        )

        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                BarChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            },

            // UPDATE COM DADOS REAIS - PARA PRODUCAO

//            update = { chart ->
//                val entriesAtual = barChartState.entriesAtual
//                val entriesPassado = barChartState.entriesPassado
//                val labels = barChartState.labels
//
//                if (entriesAtual.isEmpty() && entriesPassado.isEmpty()) {
//                    chart.clear()
//                    return@AndroidView
//                }
//
//                val setAtual = BarDataSet(entriesAtual, "Atual").apply {
//                    color = android.graphics.Color.rgb(60, 130, 230)
//                }
//
//                val setPassado = BarDataSet(entriesPassado, "Anterior").apply {
//                    color = android.graphics.Color.LTGRAY
//                }
//
//                val barData = BarData(setPassado, setAtual).apply {
//                    barWidth = 0.3f
//                }
//
//                chart.data = barData
//
//                chart.xAxis.apply {
//                    valueFormatter = IndexAxisValueFormatter(labels)
//                    granularity = 1f
//                    setCenterAxisLabels(true)
//                    setDrawGridLines(false)
//                    position = XAxis.XAxisPosition.BOTTOM
//                    axisMinimum = 0f // <- ESSENCIAL!
//                }
//
//                chart.axisLeft.axisMinimum = 0f
//                chart.axisRight.isEnabled = false
//                chart.description.isEnabled = false
//
//                val groupSpace = 0.3f
//                val barSpace = 0.05f
//                val groupCount = labels.size
//
//                // Isso garante que os grupos estejam visíveis corretamente
//                chart.setVisibleXRangeMaximum(groupCount.toFloat())
//                chart.groupBars(0f, groupSpace, barSpace)
//
//                chart.invalidate()
//            }



            // UPDATE COM DADOS FICTICIOS - PARA DEMONSTRAÇÃO
            update = { chart ->
                val labels = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom")
                val taxasPassado = listOf(70f, 65f, 80f, 75f, 60f, 85f, 90f)
                val taxasAtual = listOf(80f, 70f, 85f, 60f, 65f, 88f, 95f)

                val entriesAtual = taxasAtual.mapIndexed { index, taxa ->
                    BarEntry(index.toFloat(), taxa)
                }
                val entriesPassado = taxasPassado.mapIndexed { index, taxa ->
                    BarEntry(index.toFloat(), taxa)
                }

                val dataSetAtual = BarDataSet(entriesAtual, "Atual").apply {
                    color = android.graphics.Color.rgb(60, 130, 230)
                }
                val dataSetPassado = BarDataSet(entriesPassado, "Anterior").apply {
                    color = android.graphics.Color.LTGRAY
                }

                val barData = BarData(dataSetPassado, dataSetAtual).apply {
                    barWidth = 0.3f
                }

                chart.data = barData

                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels) // ← Usa os mesmos labels
                    granularity = 1f
                    setCenterAxisLabels(true)
                    setDrawGridLines(false)
                    position = XAxis.XAxisPosition.BOTTOM
                }

                chart.axisLeft.axisMinimum = 0f
                chart.axisRight.isEnabled = false
                chart.description.isEnabled = false

                val groupSpace = 0.3f
                val barSpace = 0.05f
                val groupCount = labels.size
                chart.setVisibleXRangeMaximum(groupCount.toFloat())
                chart.groupBars(0f, groupSpace, barSpace)
                chart.invalidate()
            }


        )

        Spacer(modifier = Modifier.height(54.dp))
    }
}

@Composable
fun RankingTypeSelector(
    selected: RankingRange,
    onSelect: (RankingRange) -> Unit
) {
    val options = listOf(
        RankingRange.DIARIO,
        RankingRange.SEMANAL,
        RankingRange.MENSAL,
        RankingRange.ANUAL
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = MaterialTheme.colorScheme.surfaceBright, shape = RoundedCornerShape(24.dp))
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selected

                Button(
                    onClick = { onSelect(option) },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = option.displayName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun DateNavigator(
    range: RankingRange,
    currentDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val label = when (range) {
        RankingRange.DIARIO -> if (currentDate == LocalDate.now()) "Hoje" else currentDate.format(DateTimeFormatter.ofPattern("dd MMM", Locale("pt", "BR")))
        RankingRange.SEMANAL -> if (isCurrentWeek(currentDate)) "Esta semana" else "Semana de"
        RankingRange.MENSAL -> currentDate.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR")).replaceFirstChar { it.uppercase() }
        RankingRange.ANUAL -> currentDate.year.toString()
    }

    val sublabel = when (range) {
        RankingRange.DIARIO -> currentDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM", Locale("pt", "BR")))
        RankingRange.SEMANAL -> {
            val startOfWeek = currentDate.with(DayOfWeek.MONDAY)
            val endOfWeek = currentDate.with(DayOfWeek.SUNDAY)
            "${startOfWeek.format(DateTimeFormatter.ofPattern("dd MMM", Locale("pt", "BR")))} – ${endOfWeek.format(DateTimeFormatter.ofPattern("dd MMM", Locale("pt", "BR")))}"
        }
        RankingRange.MENSAL -> {
            val month = currentDate.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
            "01 – ${currentDate.lengthOfMonth()} $month"
        }
        RankingRange.ANUAL -> "01 jan – 31 dez"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = sublabel,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
            )
        }

        Row {
            Button(
                onClick = { onDateChange(range.previous(currentDate)) },
                modifier = Modifier.size(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                ),
                border = BorderStroke(1.dp, Color(0xFFEAECF0)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = LineAwesomeIcons.AngleLeftSolid,
                    contentDescription = "Antes",
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onDateChange(range.next(currentDate)) },
                modifier = Modifier.size(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                ),
                border = BorderStroke(1.dp, Color(0xFFEAECF0)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = LineAwesomeIcons.AngleRightSolid,
                    contentDescription = "Próximo",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun StatsCard(
    selectedCategoria: String = "Todos os Hábitos",
    onCategoriaChange: (String) -> Unit = {},
    taxaSucesso: Int = 75,
    pontos: Int = 150,
    completados: Int = 5,
    pulados: Int = 2
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {}

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = selectedCategoria,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Resumo",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Box {
                TextButton(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, Color(0xFFEAECF0)),
                ) {
                    var rotateIcon = if (expanded) 180f else 0f
                    Icon(
                        imageVector = LineAwesomeIcons.AngleDownSolid,
                        contentDescription = "Selecionar Categoria",
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(rotateIcon),
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    listOfCategories.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                onCategoriaChange(it)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("TAXA DE SUCESSO", fontSize = 14.sp, color = Color.Gray)
                Text("$taxaSucesso%", fontSize = 18.sp, color = Color(0xFF5EC900), fontWeight = FontWeight.Bold)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("PONTOS CONQUISTADOS", fontSize = 14.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFFFFD500), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {}
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("$pontos", fontSize = 18.sp, color = Color(0xFFF2B600), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("HÁBITOS CONCLUÍDOS", fontSize = 14.sp, color = Color.Gray)
                Text("$completados", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("HÁBITOS PULADOS", fontSize = 14.sp, color = Color.Gray)
                Text("$pulados", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Red)
            }
        }
    }
}

enum class RankingRange(val displayName: String) {
    DIARIO("Diário"),
    SEMANAL("Semanal"),
    MENSAL("Mensal"),
    ANUAL("Anual");

    fun previous(date: LocalDate): LocalDate = when (this) {
        DIARIO -> date.minusDays(1)
        SEMANAL -> date.minusWeeks(1)
        MENSAL -> date.minusMonths(1)
        ANUAL -> date.minusYears(1)
    }

    fun next(date: LocalDate): LocalDate = when (this) {
        DIARIO -> date.plusDays(1)
        SEMANAL -> date.plusWeeks(1)
        MENSAL -> date.plusMonths(1)
        ANUAL -> date.plusYears(1)
    }
}

fun isCurrentWeek(date: LocalDate): Boolean {
    val now = LocalDate.now()
    val thisMonday = now.with(DayOfWeek.MONDAY)
    val thisSunday = now.with(DayOfWeek.SUNDAY)
    return date in thisMonday..thisSunday
}


@Preview(showBackground = true)
@Composable
fun PreviewRankingScreen() {
    RankingScreen()
}