package br.com.app.src.main.kotlin.com.habitus.presentation.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast


@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var notificationsEnabled by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item { SectionTitle("Perfil") }

            item {
                SettingsItem("Editar perfil") {
                    Toast.makeText(context, "Abrir edição de perfil", Toast.LENGTH_SHORT).show()
                }
            }

            item { SectionTitle("Aparência") }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isDarkTheme) "Tema: Escuro" else "Tema: Claro",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = {
                            isDarkTheme = it
                            Toast.makeText(
                                context,
                                if (it) "Tema Escuro ativado" else "Tema Claro ativado",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    )
                }
            }

            item { SectionTitle("Notificações") }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Permitir notificações",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = {
                            notificationsEnabled = it
                            Toast.makeText(
                                context,
                                if (it) "Notificações ativadas" else "Notificações desativadas",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }

            item { SectionTitle("Dados") }

            item {
                SettingsItem("Exportar relatório de hábitos") {
                    Toast.makeText(context, "Exportar relatório", Toast.LENGTH_SHORT).show()
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }

            item { SectionTitle("Conta") }

            item {
                SettingsItem("Alterar senha") {
                    Toast.makeText(context, "Alterar senha", Toast.LENGTH_SHORT).show()
                }
            }

            item {
                SettingsItem("Logout") {
                    Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        maxLines = 1
    )
}

@Composable
fun SettingsItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
