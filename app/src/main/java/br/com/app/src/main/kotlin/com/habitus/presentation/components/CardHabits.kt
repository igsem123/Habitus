package br.com.app.src.main.kotlin.com.habitus.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.helpers.toWeekdayNamesPtList
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme
import br.com.app.src.main.kotlin.com.habitus.ui.theme.SuccessColor
import br.com.app.src.main.kotlin.com.habitus.ui.theme.SuccessGreen
import br.com.app.src.main.kotlin.com.habitus.ui.theme.TonalDarkGreen
import compose.icons.AllIcons
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CheckSolid
import compose.icons.lineawesomeicons.PlusSolid
import compose.icons.lineawesomeicons.Smile
import compose.icons.lineawesomeicons.TrashSolid

@Composable
fun CardHabits(
    habit: HabitEntity,
    onCheckHabit: (Boolean) -> Unit = {},
    onDeleteHabit: (Long) -> Unit = {},
    settingsViewModel: SettingsViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(habit.isCompleted) }
    val days = habit.days.toWeekdayNamesPtList()
    val darkTheme by settingsViewModel.darkTheme.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .shadow(
                elevation = if (isChecked) 0.dp else 0.5.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isChecked) {
                    if (darkTheme) TonalDarkGreen else SuccessColor
                } else {
                    MaterialTheme.colorScheme.background
                },
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable {
                expanded = !expanded
            }
            .animateContentSize(),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val iconMap = LineAwesomeIcons.AllIcons.associateBy { it.name }
                val icon = iconMap[habit.icon] ?: LineAwesomeIcons.Smile
                Icon(
                    imageVector = icon,
                    contentDescription = "Ícone do hábito",
                    modifier = Modifier
                        .size(28.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            MaterialTheme.shapes.small
                        )
                        .padding(4.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Text(
                    text = habit.title,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Icon(
                    imageVector = if (isChecked) LineAwesomeIcons.CheckSolid else LineAwesomeIcons.PlusSolid,
                    contentDescription = null,
                    tint = if (isChecked) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (isChecked) SuccessGreen else MaterialTheme.colorScheme.surfaceBright,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                        .clickable {
                            isChecked = !isChecked
                            onCheckHabit(isChecked)
                        }
                )
            }

            if (expanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = "Categoria: \n${habit.category}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Frequência: \n${habit.frequency}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Dias da semana:",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Row {
                        days.forEach { day ->
                            Text(
                                text = day,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .padding(horizontal = 2.dp, vertical = 4.dp)
                                    .background(
                                        color = if (isChecked) MaterialTheme.colorScheme.background.copy(
                                            0.6f
                                        ) else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Descrição: \n${habit.description}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Icon(
                    imageVector = LineAwesomeIcons.TrashSolid,
                    contentDescription = "Excluir hábito",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = if (isChecked) MaterialTheme.colorScheme.background.copy(0.6f) else Color.White,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                        .clickable {
                            // Ação de exclusão do hábito
                            onDeleteHabit(habit.id)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CardHabitsPreview() {
    HabitusTheme {
        CardHabits(
            habit = HabitEntity(
                id = 1L,
                title = "Exercício",
                description = "Praticar exercícios físicos diariamente",
                isCompleted = false,
                category = "Saúde",
                frequency = "Diária",
                pontuation = 10,
                days = listOf(1, 2, 3, 4, 5, 6, 7),
                icon = "LineAwesomeIcons.HandPointUp"
            ),
            onCheckHabit = {},
            settingsViewModel = hiltViewModel<SettingsViewModel>()
        )
    }
}