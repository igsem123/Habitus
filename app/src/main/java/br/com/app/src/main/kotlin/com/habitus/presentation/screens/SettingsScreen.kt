package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToInitialForm
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import androidx.core.content.edit


@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var showEditDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    val viewModel: SettingsViewModel = hiltViewModel()

    var darkTheme by remember { mutableStateOf(prefs.getBoolean("dark_theme", false)) }
    var allowNotifications by remember {
        mutableStateOf(
            prefs.getBoolean(
                "allow_notifications",
                true
            )
        )
    }

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
                        displayName = newName
                    }
                    user.updateProfile(profileUpdate)
                        .addOnSuccessListener { name = newName }
                        .addOnFailureListener {
                            Toast.makeText(context, "Erro ao atualizar nome", Toast.LENGTH_SHORT)
                                .show()
                        }
                }

                if (newEmail.isNotBlank() && newEmail != email) {
                    if (password.isBlank()) {
                        Toast.makeText(
                            context,
                            "Informe a senha para alterar o e-mail",
                            Toast.LENGTH_LONG
                        ).show()
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

                                Toast.makeText(
                                    context,
                                    "Senha incorreta. Não foi possível reautenticar.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }

                if (newName.isBlank() && (newEmail.isBlank() || newEmail == email)) {
                    Toast.makeText(context, "Nenhuma alteração detectada", Toast.LENGTH_SHORT)
                        .show()
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
                            Toast.makeText(context, "Erro ao alterar senha", Toast.LENGTH_SHORT)
                                .show()
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
                Text(
                    text = "Tema escuro",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                )
                Switch(
                    checked = darkTheme,
                    onCheckedChange = {
                        darkTheme = it
                        prefs.edit { putBoolean("dark_theme", it) }
                        if (it) {
                            Toast.makeText(context, "Tema escuro ativado", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "Tema claro ativado", Toast.LENGTH_SHORT).show()
                        }

                        viewModel.setDarkTheme(it)
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
                Text(
                    text = "Permitir notificações",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                )
                Switch(
                    checked = allowNotifications,
                    onCheckedChange = {
                        allowNotifications = it
                        prefs.edit { putBoolean("allow_notifications", it) }
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
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
    )
}

@Composable
fun EditProfileDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Editar Perfil",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Senha (para alterar e-mail)") }, singleLine = true, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
            }
        },

        confirmButton = {
            TextButton(onClick = { onSave(name, email, password) }) {
                Text(
                    text = "Salvar",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    )
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit, onChange: (String, String) -> Unit) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Alterar Senha",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        text = {
            Column {
                OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("Nova senha") }, singleLine = true, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
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
                Text(
                    text = "Alterar",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
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
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        )
    }
}

fun logout(context: Context) {
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    prefs.edit().remove(
        "" +
                "user_token"
    ).apply()
}
