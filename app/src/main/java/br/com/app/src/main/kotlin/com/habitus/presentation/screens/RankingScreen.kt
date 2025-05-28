package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import java.time.LocalDate


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter

//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun RankingScreen(
    selectedRange: RankingRange = RankingRange.SEMANAL,
    selectedDate: LocalDate = LocalDate.now(),
    selectedHabit: String = "Todos os hábitos",
    onRangeChange: (RankingRange) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onHabitChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Selecionador de tipo de ranking
        RankingTypeSelector(selectedRange, onRangeChange)

        Spacer(modifier = Modifier.height(16.dp))

        // Texto com data atual e setas para navegar
        DateNavigator(selectedRange, selectedDate, onDateChange)

        Spacer(modifier = Modifier.height(16.dp))

        // Card com estatísticas
        StatsCard(
//            selectedHabit = selectedHabit,
//            onHabitChange = onHabitChange,
//            stats = HabitStats(85, 120, 12, 3) // Dados fictícios
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card para gráfico (placeholder)
        Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text("Gráfico será implementado aqui")
            }
        }
    }
}

@Composable
fun RankingTypeSelector(
    selected: RankingRange,
    onSelect: (RankingRange) -> Unit
) {
    val options = listOf(RankingRange.DIARIO, RankingRange.SEMANAL, RankingRange.MENSAL, RankingRange.ANUAL)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = Color(0xFFEAECF0), shape = RoundedCornerShape(24.dp))
            .padding(4.dp) // Espaço interno entre fundo e botões
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp) // Espaço entre botões
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
                        containerColor = if (isSelected) Color.White else Color.Transparent,
                        contentColor = if (isSelected) Color(0xFF3843FF) else Color(0xFF686873)
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = option.displayName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}




//@Composable
//fun DateNavigator(
//    range: RankingRange,
//    currentDate: LocalDate,
//    onDateChange: (LocalDate) -> Unit
//) {
//    val formatter = range.getFormatter()
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        IconButton(onClick = { onDateChange(range.previous(currentDate)) }) {
//            Icon(Icons.Filled.ArrowBack, contentDescription = "Anterior")
//        }
//        Text("Essa ${range.displayName.lowercase()} - ${currentDate.format(formatter)}")
//        IconButton(onClick = { onDateChange(range.next(currentDate)) }) {
//            Icon(Icons.Filled.ArrowForward, contentDescription = "Próxima")
//        }
//    }
//}

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
        // Textos alinhados à esquerda
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = sublabel,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Botões com estilo quadrado e bordas arredondadas
        Row {
            Button(
                onClick = { onDateChange(range.previous(currentDate)) },
                modifier = Modifier
                    .size(36.dp), // Largura = Altura = quadrado
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF686873)
                ),
                border = BorderStroke(1.dp, Color(0xFFEAECF0)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("<")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onDateChange(range.next(currentDate)) },
                modifier = Modifier
                    .size(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF686873)
                ),
                border = BorderStroke(1.dp, Color(0xFFEAECF0)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(">")
            }
        }
    }
}



//@Composable
//fun DateNavigator(
//    range: RankingRange,
//    currentDate: LocalDate,
//    onDateChange: (LocalDate) -> Unit
//) {
//    val formatter = range.getFormatter()
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        // Texto com a data formatada
//        Text("Essa ${range.displayName.lowercase()} - ${currentDate.format(formatter)}")
//
//        // Usando apenas Text ou Button para navegação
//        Button(
//            onClick = { onDateChange(range.previous(currentDate)) },
//            modifier = Modifier.padding(end = 8.dp) // Adiciona um pequeno espaçamento
//        ) {
//            Text("<")
//        }
//
//        Button(
//            onClick = { onDateChange(range.next(currentDate)) },
//            modifier = Modifier.padding(start = 8.dp) // Adiciona um pequeno espaçamento
//        ) {
//            Text(">")
//        }
//    }
//}

@Composable
fun StatsCard(
    selectedCategoria: String = "Todos os Hábitos",
    onCategoriaChange: (String) -> Unit = {},
    taxaSucesso: Int = 75,
    pontos: Int = 150,
    completados: Int = 5,
    pulados: Int = 2
) {
    val categorias = listOf("Todos os Hábitos", "Saúde", "Produtividade", "Bem-estar")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        // Linha superior
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Ícone arredondado
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
//                Icon(
//                    imageVector = Icons.Default.Star,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary
//                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Título e subtítulo
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = selectedCategoria,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Resumo",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Dropdown
            Box {
                TextButton(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Gray
                    ),
                    border = BorderStroke(1.dp, Color(0xFFEAECF0)),
                ) {
                    Text("▼")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach {
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

        // Linha 1 - Sucesso e Pontos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("TAXA DE SUCESSO", fontSize = 12.sp, color = Color.Gray)
                Text("$taxaSucesso%", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("PONTOS CONQUISTADOS", fontSize = 12.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFFFFF9C4), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
//                        Icon(
//                            //imageVector = Icons.Default.EmojiEvents,
//                            contentDescription = null,
//                            tint = Color(0xFFFFC107),
//                            modifier = Modifier.size(16.dp)
//                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "$pontos",
                        fontSize = 16.sp,
                        color = Color(0xFFFFC107),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Linha 2 - Completados e Pulados
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("HÁBITOS COMPLETADOS", fontSize = 12.sp, color = Color.Gray)
                Text("$completados", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("HÁBITOS PULADOS", fontSize = 12.sp, color = Color.Gray)
                Text(
                    "$pulados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
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

    fun getFormatter(): DateTimeFormatter = when (this) {
        DIARIO -> DateTimeFormatter.ofPattern("dd/MM/yyyy")
        SEMANAL -> DateTimeFormatter.ofPattern("'semana de' dd/MM")
        MENSAL -> DateTimeFormatter.ofPattern("MMMM/yyyy")
        ANUAL -> DateTimeFormatter.ofPattern("yyyy")
    }
}

data class HabitStats(
    val successRate: Int,
    val points: Int,
    val completed: Int,
    val skipped: Int
)

fun isCurrentWeek(date: LocalDate): Boolean {
    val now = LocalDate.now()
    val thisMonday = now.with(DayOfWeek.MONDAY)
    val thisSunday = now.with(DayOfWeek.SUNDAY)
    return date in thisMonday..thisSunday
}

@Preview(showBackground = true)
@Composable
fun PreviewRankingScreen() {
    RankingScreen(
        selectedRange = RankingRange.SEMANAL,
        selectedDate = LocalDate.now(),
        selectedHabit = "Exercício",
        onRangeChange = {},
        onDateChange = {},
        onHabitChange = {}
    )
}