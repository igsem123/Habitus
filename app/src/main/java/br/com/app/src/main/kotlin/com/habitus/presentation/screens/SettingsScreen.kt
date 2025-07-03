package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToInitialForm
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var showEditDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    val viewModel: SettingsViewModel = hiltViewModel()

    var darkTheme by remember { mutableStateOf(prefs.getBoolean("dark_theme", false)) }
    var allowNotifications by remember { mutableStateOf(prefs.getBoolean("allow_notifications", true)) }

    var name by remember { mutableStateOf(user?.displayName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }

    if (user == null) {
        Toast.makeText(context, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
        return
    }

    if (showEditDialog) {
        EditProfileDialog(
            onDismiss = { showEditDialog = false },
            onSave = { newName, newEmail, password ->
                if (newName.isNotBlank()) {
                    val profileUpdate = userProfileChangeRequest {
                        setDisplayName(newName)
                    }
                    user.updateProfile(profileUpdate)
                        .addOnSuccessListener { name = newName }
                        .addOnFailureListener {
                            Toast.makeText(context, "Erro ao atualizar nome", Toast.LENGTH_SHORT).show()
                        }
                }

                if (newEmail.isNotBlank() && newEmail != email) {
                    if (password.isBlank()) {
                        Toast.makeText(context, "Informe a senha para alterar o e-mail", Toast.LENGTH_LONG).show()
                    } else {
                        val credential = EmailAuthProvider.getCredential(user.email!!, password)
                        user.reauthenticate(credential)
                            .addOnSuccessListener {
                                user.verifyBeforeUpdateEmail(newEmail)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Verifique o novo email. Um link foi enviado.", Toast.LENGTH_LONG).show()
                                    }
                                    .addOnFailureListener { ex ->
                                        Log.e("SettingsScreen", "Erro ao solicitar verificação", ex)
                                        Toast.makeText(context, "Erro: ${ex.message}", Toast.LENGTH_LONG).show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Senha incorreta.", Toast.LENGTH_LONG).show()
                            }
                    }
                }

                if (newName.isBlank() && (newEmail.isBlank() || newEmail == email)) {
                    Toast.makeText(context, "Nenhuma alteração detectada", Toast.LENGTH_SHORT).show()
                }

                showEditDialog = false
            }
        )
    }

    if (showPasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showPasswordDialog = false },
            onChange = { newPass, confirmPass ->
                if (newPass == confirmPass) {
                    user?.updatePassword(newPass)
                        ?.addOnSuccessListener {
                            Toast.makeText(context, "Senha alterada", Toast.LENGTH_SHORT).show()
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(context, "Erro ao alterar senha", Toast.LENGTH_SHORT).show()
                        }
                    showPasswordDialog = false
                } else {
                    Toast.makeText(context, "Senhas não coincidem", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionTitle("Perfil")
            SettingsItem("Editar Perfil") { showEditDialog = true }
        }

        item {
            SectionTitle("Tema")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tema escuro", modifier = Modifier.weight(1f))
                Switch(
                    checked = darkTheme,
                    onCheckedChange = {
                        darkTheme = it
                        prefs.edit().putBoolean("dark_theme", it).apply()
                        AppCompatDelegate.setDefaultNightMode(
                            if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }
                )
            }
        }

        item {
            SectionTitle("Notificações")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Permitir notificações", modifier = Modifier.weight(1f))
                Switch(
                    checked = allowNotifications,
                    onCheckedChange = {
                        allowNotifications = it
                        prefs.edit().putBoolean("allow_notifications", it).apply()
                        if (it) {
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                context,
                                "Notificações desativadas. Ative nas configurações do sistema.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }
        }

        item {
            SectionTitle("Dados")
        }

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

        item {
            SectionTitle("Segurança")
            SettingsItem("Alterar Senha") { showPasswordDialog = true }
        }

        item {
            SectionTitle("Conta")
            SettingsItem("Sair da Conta") {
                auth.signOut()
                navController.navigateToInitialForm()
            }
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
            .padding(top = 16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun EditProfileDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Perfil") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Senha (para alterar e-mail)") }, singleLine = true, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
            }
        },
        confirmButton = { TextButton(onClick = { onSave(name, email, password) }) { Text("Salvar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit, onChange: (String, String) -> Unit) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Alterar Senha") },
        text = {
            Column {
                OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("Nova senha") }, singleLine = true, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar senha") }, singleLine = true, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
            }
        },
        confirmButton = { TextButton(onClick = { onChange(newPassword, confirmPassword) }) { Text("Alterar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
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
        Text(text = text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
    }
}

fun logout(context: Context) {
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    prefs.edit().remove("user_token").apply()
}
