package com.tbart.pushup.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SessionUiState(
    val isLoading: Boolean = false,
    val sessionId: Int? = null,
    val title: String = "",
    val date: String = "",
    val exercises: List<String> = emptyList(), // temporaire
    val errorMessage: String? = null
)

class SessionViewModel(
    // private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    fun createNewSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO : appeler repository pour créer la session
                // val newSession = sessionRepository.createNewSession()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    sessionId = 1, // exemple
                    title = "Nouvelle séance",
                    date = "Aujourd'hui"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Impossible de créer la séance"
                )
            }
        }
    }

    fun loadSessionDetails(sessionId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO : charger la séance depuis le repository
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    sessionId = sessionId,
                    title = "Séance du 12 août",
                    date = "12/08/2025",
                    exercises = listOf("Pompes", "Squats", "Planche")
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur de chargement"
                )
            }
        }
    }
}