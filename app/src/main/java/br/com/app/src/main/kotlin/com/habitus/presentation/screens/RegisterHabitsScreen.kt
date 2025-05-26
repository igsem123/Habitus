@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import android.widget.Toast
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
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.app.src.main.kotlin.com.habitus.data.entity.Days
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.HabitsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterHabitsScreen(
    modifier: Modifier = Modifier,
    user: UserEntity,
    viewModel: HabitsViewModel = hiltViewModel(),
    onHabitRegistered: () -> Unit // Callback para ação após o registro do hábito
) {
    val uiState by viewModel.registerUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val gradient = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.primary
        )
    )

    // Efeito para lidar com o sucesso do salvamento ou erros
    LaunchedEffect(uiState.saveSuccess, uiState.error) {
        if (uiState.saveSuccess) {
            Toast.makeText(context, "Hábito registrado com sucesso!", Toast.LENGTH_SHORT).show()
            onHabitRegistered() // Executa a ação de callback
            viewModel.resetRegisterState()
        }
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetError() // Limpa o erro após exibição
        }
    }

    if (uiState.showIconSelectorDialog) {
        IconSelectorDialog(
            icons = uiState.availableIcons,
            onIconSelected = { icon ->
                viewModel.onIconSelected(icon)
            },
            onDismiss = { viewModel.dismissIconDialog() }
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
                onClick = { viewModel.showIconDialog() },
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
                    imageVector = uiState.selectedIcon,
                    contentDescription = "Selecionar ícone do hábito",
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }

            HabitFormTextField(
                value = uiState.habitName,
                onValueChange = { viewModel.onHabitNameChange(it) },
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
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                uiState.frequencyOptions.forEach { frequency ->
                    FrequencyButton(
                        frequency = frequency,
                        onSelectFrequency = { viewModel.onFrequencyChange(frequency) },
                        selected = uiState.selectedFrequency == frequency
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
                    onSelectDay = { viewModel.onDaySelected(day) },
                    selected = uiState.selectedDays.contains(day)
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
                checked = uiState.selectedDays.size == Days.entries.size,
                onCheckedChange = { isChecked ->
                    viewModel.onToggleDaily(isChecked)
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
                value = uiState.pontuation,
                onValueChange = { newValue ->
                    viewModel.onPontuationChange(newValue)
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
            value = uiState.habitDescription,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = "Descrição do hábito",
            placeholder = "Ex: Beber água ao acordar",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Text(
            text = "Selecione a categoria",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
        )

        ExposedDropdownMenuBox(
            expanded = uiState.isDropdownMenuExpanded,
            onExpandedChange = { viewModel.onToggleCategoryDropdown(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                value = uiState.selectedCategory,
                onValueChange = { }, // Vazio para n permitir edição direta do campo de texto
                readOnly = true,
                label = { Text("Categoria") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isDropdownMenuExpanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                shape = RoundedCornerShape(16.dp),
            )
            ExposedDropdownMenu(
                expanded = uiState.isDropdownMenuExpanded,
                onDismissRequest = { viewModel.onToggleCategoryDropdown(false) },
                modifier = Modifier
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                uiState.categories.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = { viewModel.onCategoryChanged(option) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.tertiary,
                        )
                    )
                }
            }
        }

        TextButton(
            onClick = {
                if (!uiState.isSaving) { // Verifica se já não está salvando para evitar múltiplos cliques
                    viewModel.attemptRegisterHabit(userId = user.uid)
                }
            },
            modifier = Modifier
                .wrapContentWidth()
                .padding(24.dp)
                .background(brush = gradient, shape = RoundedCornerShape(16.dp))
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            enabled = !uiState.isSaving // Desabilita o botão enquanto está salvando
        ) {
            if (uiState.isSaving) {
                CircularWavyProgressIndicator(
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp),
                )
            } else {
                Text(
                    text = "Adicionar Hábito",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(4.dp),
                )
            }
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
            style = MaterialTheme.typography.bodyMedium
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
            modifier = modifier
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
                    text = "Selecione um ícone:",
                    style = MaterialTheme.typography.headlineMedium,
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