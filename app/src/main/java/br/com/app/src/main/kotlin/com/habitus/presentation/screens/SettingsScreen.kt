package br.com.app.src.main.kotlin.com.habitus.presentation.screens


import android.content.Context
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
import androidx.navigation.NavController
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.PasswordVisualTransformation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToInitialForm
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel
import androidx.hilt.navigation.compose.hiltViewModel




@Composable
fun SettingsScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = hiltViewModel()
    var notificationsEnabled by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(false) }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            //Perfil
            item { SectionTitle("Perfil") }

            item {
                SettingsItem("Editar perfil") {
                    showEditProfileDialog = true
                }
            }

            //Aparência
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

            //Notificações
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

            //Exportar relatório
            item { SectionTitle("Dados") }

            item {
                SettingsItem("Exportar relatório de hábitos") {
                    viewModel.exportReport(
                        context = context,
                        onSuccess = {
                            Toast.makeText(context, "Relatório exportado com sucesso!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { error ->
                            Toast.makeText(context, "Erro: $error", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

            }

            item { SectionTitle("Conta") }

            //Alterar senha e sair da conta
            item {
                SettingsItem("Alterar senha")  {
                    showChangePasswordDialog = true
                }
            }

            item {
                SettingsItem("Sair da conta") {
                    logout(context)
                    Toast.makeText(context, "Você foi deslogado", Toast.LENGTH_SHORT).show()
                    navController.navigateToInitialForm()  //Navega para a tela inicial
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }

if (showEditProfileDialog) {
    EditProfileDialog(
        onDismiss = { showEditProfileDialog = false },
        onSave = { name, email ->
            Toast.makeText(context, "Nome salvo: $name\nEmail salvo: $email", Toast.LENGTH_SHORT).show()
            showEditProfileDialog = false
        }
    )
}

if (showChangePasswordDialog) {
    ChangePasswordDialog(
        onDismiss = { showChangePasswordDialog = false },
        onChange = { newPassword, confirmPassword ->
            if (newPassword == confirmPassword && newPassword.isNotBlank()) {
                Toast.makeText(context, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                showChangePasswordDialog = false
            } else {
                Toast.makeText(context, "As senhas não coincidem ou estão vazias.", Toast.LENGTH_SHORT).show()
            }
        }
    )
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
//Editar Perfil: edição de nome e e-mail
fun EditProfileDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Perfil") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, email) }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
//Alterar senha e confirmar alteração
fun ChangePasswordDialog(onDismiss: () -> Unit, onChange: (String, String) -> Unit) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Alterar Senha") },
        text = {
            Column {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nova senha") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar senha") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onChange(newPassword, confirmPassword) }) {
                Text("Alterar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
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

////Remove o token de autenticação salvo localmente.
////A associação do token com o usuário deve ser tratada no backend.
fun logout(context: Context) {
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    prefs.edit().remove("" +
            "user_token").apply()
}