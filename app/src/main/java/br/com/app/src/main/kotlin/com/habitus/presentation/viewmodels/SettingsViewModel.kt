package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
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