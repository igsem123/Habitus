package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepository
import android.content.Intent
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
  @ApplicationContext private val context: Context
) : ViewModel() {

    fun exportReport(
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) { // ✅ Aqui está a correção!
            try {
                val report = habitRepository.getHabitsReport()

                val file = File(context.getExternalFilesDir(null), "relatorio_habitos.txt")
                file.writeText(report)

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                context.startActivity(Intent.createChooser(intent, "Compartilhar relatório"))

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }

    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _darkTheme = MutableStateFlow(prefs.getBoolean("dark_theme", false))
    val darkTheme: StateFlow<Boolean> = _darkTheme

    private val _allowNotifications = MutableStateFlow(prefs.getBoolean("allow_notifications", true))
    val allowNotifications: StateFlow<Boolean> = _allowNotifications

    fun setDarkTheme(enabled: Boolean) {
        _darkTheme.value = enabled
        prefs.edit { putBoolean("dark_theme", enabled) }
    }

    fun setAllowNotifications(enabled: Boolean) {
        _allowNotifications.value = enabled
        prefs.edit { putBoolean("allow_notifications", enabled) }
    }
}
