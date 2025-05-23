package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.app.src.main.kotlin.com.habitus.R
import br.com.app.src.main.kotlin.com.habitus.presentation.components.CalendarioComponent
import br.com.app.src.main.kotlin.com.habitus.presentation.components.CardHabits
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.PlusSolid

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(userName: String = "Usuário") {
    val habits = listOf("Ler", "Meditar", "Caminhar", "Beber água")
    val checkedHabits = remember { mutableStateOf(setOf("Meditar")) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = rememberLazyListState(),
    ) {
        // Topo azul com curva
        item {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(120.dp)
                    .fillMaxWidth()
                    .paint(
                        painter = painterResource(R.drawable.bc_top_appbar),
                        contentScale = ContentScale.FillWidth,
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary)
                    ),
            ) {
                // Botão flutuante de adicionar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 20.dp)
                        .size(50.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        LineAwesomeIcons.PlusSolid,
                        contentDescription = "Adicionar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp)
                            .clickable {

                            }
                    )
                }

                // Nome do usuário e instrução
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, bottom = 8.dp)
                ) {
                    Text("Olá, $userName!", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Vamos criar hábitos!", color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            CalendarioComponent(
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Meta diária
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.primary
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                val completed = checkedHabits.value.size
                val total = habits.size
                val progress by animateFloatAsState(
                    targetValue = if (total > 0) completed.toFloat() / total else 0f,
                    label = "progress"
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularWavyProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(4.dp),
                        color = Color.White,
                        waveSpeed = 8.dp
                    )
                    Text(
                        text = "Sua meta diária!\n$completed de $total hábitos",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Lista de hábitos
        item {
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
                Text(
                    text = "Hábitos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                habits.forEach { habit ->
                    val isChecked = checkedHabits.value.contains(habit)
                    CardHabits(isChecked, checkedHabits, habit)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HabitsScreenPreview() {
    HabitusTheme {
        HomeScreen(userName = "Maria")
    }
}
