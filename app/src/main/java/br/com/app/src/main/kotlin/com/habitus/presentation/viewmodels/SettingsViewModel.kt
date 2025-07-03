package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepository
import android.content.Intent
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val habitRepository: HabitRepository
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

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "text/plain")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                }

                context.startActivity(intent)

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }
}
