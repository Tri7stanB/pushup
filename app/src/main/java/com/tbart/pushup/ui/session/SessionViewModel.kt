package com.tbart.pushup.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbart.pushup.domain.model.Exercise
import com.tbart.pushup.domain.model.Session
import com.tbart.pushup.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SessionUiState(
    val isLoading: Boolean = false,
    val sessionId: Int? = null,
    val title: String = "",
    val date: String = "",
    val exercises: List<Exercise> = emptyList(),
    val errorMessage: String? = null
)

class SessionViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    private var currentSessionId: Int? = null

    fun createNewSession() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Création de la session en DB
                val newSession = Session(
                    title = "Nouvelle séance",
                    date = System.currentTimeMillis() // Utilise le timestamp actuel
                )
                sessionRepository.addSession(newSession)

                currentSessionId = newSession.id

                // Démarre l'observation des exercices liés
                observeExercises(newSession.id)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        sessionId = newSession.id,
                        title = "Nouvelle séance",
                        date = "Aujourd'hui"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Impossible de créer la séance")
                }
            }
        }
    }

    fun loadSession(sessionId: Int) {
        currentSessionId = sessionId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Charger la session existante
                val session = sessionRepository.getSessionById(sessionId)
                if (session != null) {
                    observeExercises(sessionId) // 👈 démarre l'observation en continu

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sessionId = sessionId
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Session introuvable"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erreur lors du chargement de la session"
                    )
                }
            }
        }
    }


    fun loadSessionDetails(sessionId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                currentSessionId = sessionId
                observeExercises(sessionId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        sessionId = sessionId,
                        title = "Séance du ${sessionId}", // tu peux remplacer par une vraie date
                        date = "xx/xx/xxxx"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Erreur de chargement")
                }
            }
        }
    }

    fun addExerciseToSession(name: String, repetitions: Int = 10, weight: Float = 0f) {
        val id = currentSessionId ?: return
        viewModelScope.launch {
            try {
                sessionRepository.addExercise(
                    Exercise(
                        sessionId = id,
                        name = name,
                        repetitions = repetitions,
                        weight = weight
                    )
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Erreur lors de l'ajout de l'exercice") }
            }
        }
    }

    fun updateExerciseRepetitions(exerciseId: Int, newReps: Int) {
        val updatedList = _uiState.value.exercises.map { exercise ->
            if (exercise.id == exerciseId) exercise.copy(repetitions = newReps) else exercise
        }
        _uiState.update { it.copy(exercises = updatedList) }
        // ⚠️ Ici tu pourrais aussi mettre à jour en DB via repository si tu veux persister
    }

    private fun observeExercises(sessionId: Int) {
        viewModelScope.launch {
            sessionRepository.getExercises(sessionId).collect { list ->
                _uiState.update { it.copy(exercises = list) }
            }
        }
    }
}
