package com.tbart.pushup.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tbart.pushup.data.repository.SessionRepository
import com.tbart.pushup.domain.model.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class HomeUiState(
    val isLoading: Boolean = false,
    val totalSessions: Int = 0,
    val lastSessionDate: String? = null,
    val hasActiveSession: Boolean = false,
    val activeSessionId: Int? = null,
    val newSessionId: Int? = null,
    val errorMessage: String? = null
)

class HomeViewModel(
    // Injectez ici votre repository quand vous l'aurez configuré
    private val sessionRepository: SessionRepository
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

    fun createNewSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                // Créer une nouvelle session
                val newSession = Session(
                    title = "Nouvelle séance",
                    date = System.currentTimeMillis(),
                )

                val sessionId = sessionRepository.addSession(newSession)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    newSessionId = sessionId,
                    hasActiveSession = true,
                    activeSessionId = sessionId
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Impossible de créer la séance"
                )
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(newSessionId = null)
    }

    private fun isToday(timestamp: Long): Boolean {
        val today = Calendar.getInstance()
        val date = Calendar.getInstance().apply { timeInMillis = timestamp }
        return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(timestamp: Long): Boolean {
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val date = Calendar.getInstance().apply { timeInMillis = timestamp }
        return yesterday.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
    }
}

// Factory pour HomeViewModel
class HomeViewModelFactory(
    private val sessionRepository: SessionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(sessionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}