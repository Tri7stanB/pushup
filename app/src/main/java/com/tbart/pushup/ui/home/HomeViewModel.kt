package com.tbart.pushup.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val hasActiveSession: Boolean = false,
    val lastSessionDate: String? = null,
    val totalSessions: Int = 0
)

class HomeViewModel(
    // Injectez ici votre repository quand vous l'aurez configuré
    // private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // TODO: Charger les données depuis le repository
                // val lastSession = sessionRepository.getLastSession()
                // val totalSessions = sessionRepository.getTotalSessionsCount()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    // lastSessionDate = lastSession?.date,
                    // totalSessions = totalSessions
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun startNewSession() {
        viewModelScope.launch {
            // TODO: Logique pour démarrer une nouvelle séance
            // sessionRepository.createNewSession()
        }
    }

    fun resumeSession() {
        viewModelScope.launch {
            // TODO: Logique pour reprendre une séance
        }
    }
}