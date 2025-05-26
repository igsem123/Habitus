@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.app.src.main.kotlin.com.habitus.data.entity.Days
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.HabitsViewModel
import compose.icons.AllIcons
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Smile

@Composable
fun RegisterHabitsScreen(
    modifier: Modifier = Modifier,
    user: UserEntity,
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val icons = remember { LineAwesomeIcons.AllIcons }
    var showIconSelectorDialog by rememberSaveable { mutableStateOf(false) }
    var selectedIcon by rememberSaveable { mutableStateOf<ImageVector?>(null) }
    var habitName by rememberSaveable { mutableStateOf("") }
    val frequencyOptions = listOf("Diariamente", "Semanalmente", "Mensalmente")
    var selectedFrequency by rememberSaveable { mutableStateOf(frequencyOptions.first()) }
    val selectedDays = remember { mutableStateListOf<Days>() }
    var pontuation by rememberSaveable { mutableIntStateOf(0) }

    if (showIconSelectorDialog) {
        IconSelectorDialog(
            icons = icons,
            onIconSelected = { icon ->
                selectedIcon = icon
                showIconSelectorDialog = false
            },
            onDismiss = { showIconSelectorDialog = false },
            modifier = Modifier
                .fillMaxSize()
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start)
        ) {
            IconButton(
                onClick = { showIconSelectorDialog = true },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = if (selectedIcon != null) selectedIcon!! else LineAwesomeIcons.Smile,
                    contentDescription = "Ícone indicando para selecionar o ícone do hábito",
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }

            HabitFormTextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = "Nome do hábito",
                placeholder = "Ex: Beber água",
                modifier = Modifier
                    .weight(1f),
            )
        }

        Text(
            text = "Frequência",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        )

        Card(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                frequencyOptions.forEach {
                    FrequencyButton(
                        frequency = it,
                        onSelectFrequency = {
                            selectedFrequency = it
                        },
                        selected = selectedFrequency == it
                    )
                }
            }
        }

        Text(
            text = "Nos dias da semana",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Days.entries.forEach { day ->
                DayButton(
                    day = day,
                    onSelectDay = {
                        if (selectedDays.contains(day)) {
                            selectedDays.remove(day)
                        } else {
                            selectedDays.add(day)
                        }
                    },
                    selected = selectedDays.contains(day)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ou diariamente",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .weight(1f)
            )
            Switch(
                checked = selectedDays.size == Days.entries.size,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        selectedDays.clear()
                        selectedDays.addAll(Days.entries)
                    } else {
                        selectedDays.clear()
                    }
                },
                modifier = Modifier.padding(end = 16.dp),
                colors = SwitchDefaults.colors(
                    uncheckedBorderColor = MaterialTheme.colorScheme.surfaceDim,
                    checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.primaryContainer,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                )
            )
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Text(
                text = "Defina a pontuação",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            )
            HabitFormTextField(
                value = pontuation.toString(),
                onValueChange = { newValue ->
                    pontuation = newValue.toIntOrNull() ?: 0
                },
                label = "Pontuação",
                placeholder = "Ex: 10",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            )

            Text(
                text = "Lembre-se de que hábitos diários valem mais pontos!",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "Descrição (opcional)",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        )

        HabitFormTextField(
            value = "",
            onValueChange = {},
            label = "Descrição do hábito",
            placeholder = "Ex: Beber água ao acordar",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        TextButton(
            onClick = {
                if (habitName.isNotBlank() && selectedIcon != null && pontuation > 0) {
                    viewModel.registerHabit(
                        HabitEntity(
                            title = habitName,
                            description = "",
                            category = "Personalizado",
                            pontuation = pontuation,
                            days = selectedDays.mapNotNull {
                                Days.fromValue(it.value)
                            }.map {
                                it.value
                            },
                            icon = selectedIcon!!.name,
                            userId = user.uid
                        )
                    )
                }
            },
            modifier = Modifier
                .wrapContentWidth()
                .padding(24.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text(
                text = "Adicionar Hábito",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(4.dp),
            )
        }
    }
}

@Composable
private fun HabitFormTextField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    label: String = "",
    placeholder: String = "",
    mainColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.tertiary,
    secondaryColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primaryContainer,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = secondaryColor,
            focusedBorderColor = mainColor,
            unfocusedLabelColor = secondaryColor,
            focusedLabelColor = mainColor,
            focusedPlaceholderColor = mainColor,
            unfocusedPlaceholderColor = secondaryColor,
            focusedTextColor = mainColor,
            unfocusedTextColor = secondaryColor,
        ),
        singleLine = true,
        maxLines = 1,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun FrequencyButton(
    frequency: String,
    onSelectFrequency: (String) -> Unit,
    selected: Boolean
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.surfaceDim
    }
    TextButton(
        onClick = { onSelectFrequency(frequency) },
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Text(
            text = frequency,
            color = color,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun DayButton(
    day: Days,
    onSelectDay: (Days) -> Unit,
    selected: Boolean = false
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.surfaceDim
    }
    TextButton(
        onClick = { onSelectDay(day) },
        modifier = Modifier
            .width(36.dp)
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Text(
            text = day.name.take(1),
            color = color,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun IconSelectorDialog(
    modifier: Modifier = Modifier,
    onIconSelected: (ImageVector) -> Unit,
    onDismiss: () -> Unit,
    icons: List<ImageVector>
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .size(300.dp, 400.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selecione um ícone",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    items(icons) { icon ->
                        IconButton(
                            onClick = { onIconSelected(icon) }
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Ícone ${icon.name}"
                            )
                        }
                    }
                }
            }
        }
    }
}