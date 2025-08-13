package com.tbart.pushup.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.tbart.pushup.domain.model.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SessionUiState(
    val isLoading: Boolean = false,
    val sessionId: Int? = null,
    val title: String = "",
    val date: String = "",
    val exercises: List<Exercise> = emptyList(), // temporaire
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
                    exercises = listOf(
                        Exercise(id = 1, sessionId = sessionId, name = "Pompes", repetitions = 15, weight = 0f),
                        Exercise(id = 2, sessionId = sessionId, name = "Squats", repetitions = 20, weight = 0f)
                    ) // exemple de données
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur de chargement"
                )
            }
        }
    }

    fun updateExerciseRepetitions(exerciseId: Int, newReps: Int) {
        val updatedList = _uiState.value.exercises.map { exercise ->
            if (exercise.id == exerciseId) exercise.copy(repetitions = newReps)
            else exercise
        }
        _uiState.value = _uiState.value.copy(exercises = updatedList)
    }

    fun addExerciseToSession(name: String, repetitions: Int = 10, weight: Float = 0f) {
        val currentExercises = _uiState.value.exercises.toMutableList()
        val nextId = (currentExercises.maxOfOrNull { it.id } ?: 0) + 1

        val newExercise = Exercise(
            id = nextId,
            sessionId = _uiState.value.sessionId ?: 0,
            name = name,
            repetitions = repetitions,
            weight = weight
        )

        currentExercises.add(newExercise)
        _uiState.value = _uiState.value.copy(exercises = currentExercises)
    }


}