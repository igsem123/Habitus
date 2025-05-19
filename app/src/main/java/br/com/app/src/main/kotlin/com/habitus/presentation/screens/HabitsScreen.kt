package br.com.app.src.main.kotlin.com.habitus.presentation.screens
import androidx.compose.material3.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import br.com.app.src.main.kotlin.com.habitus.ui.theme.azulMarinho
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme



@Composable
fun HabitsScreen(userName: String = "Usuário") {
    val selectedDate = remember { mutableStateOf(3) }
    val habits = listOf("Ler", "Meditar", "Caminhar", "Beber água")
    val checkedHabits = remember { mutableStateOf(setOf("Meditar")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Topo azul com curva
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(azulMarinho),
        ) {
            // Botão flutuante de adicionar
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .size(32.dp)
                    .background(Color.White, shape = CircleShape)
                    .clickable { /* ação de adicionar hábito */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = azulMarinho)
            }

            // Nome do usuário e instrução
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 8.dp)
            ) {
                Text("Olá, $userName!", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Vamos criar hábitos!", color = Color.White.copy(alpha = 0.8f))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Datas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("2\nSEX", "3\nSÁB", "4\nDOM", "5\nSEG", "6\nTER", "7\nQUA", "8\nQUI", "9\nSEX").forEachIndexed { index, day ->
                val dayNumber = 2 + index
                val isSelected = selectedDate.value == dayNumber

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) azulMarinho else Color.LightGray.copy(0.2f))
                        .clickable { selectedDate.value = dayNumber },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontSize = 12.sp,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Meta diária
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(azulMarinho, Color(0xFF004AAD))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            val completed = checkedHabits.value.size
            val total = habits.size
            Text(
                text = "Sua meta diária!\n$completed de $total hábitos",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de hábitos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Hábitos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            habits.forEach { habit ->
                val isChecked = checkedHabits.value.contains(habit)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            color = if (isChecked) Color(0xFFDFF2E1) else Color(0xFFF8F8F8),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            checkedHabits.value = if (isChecked)
                                checkedHabits.value - habit
                            else
                                checkedHabits.value + habit
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(habit, fontSize = 16.sp)
                        Icon(
                            imageVector = if (isChecked) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            tint = if (isChecked) Color.White else azulMarinho,
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = if (isChecked) Color(0xFF4CAF50) else Color.White,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HabitsScreenPreview() {
    HabitusTheme {
        HabitsScreen(userName = "Maria")
    }
}
