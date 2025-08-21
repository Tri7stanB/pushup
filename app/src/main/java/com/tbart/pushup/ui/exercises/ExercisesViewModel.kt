package com.tbart.pushup.ui.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbart.pushup.data.repository.ExerciseTemplateRepository
import com.tbart.pushup.domain.model.ExerciseTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExercisesUiState(
    val exercises: List<ExerciseTemplate> = emptyList(),
    val availableGroups: List<String> = emptyList()
)

class ExercisesViewModel(
    private val exerciseTemplateRepository: ExerciseTemplateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            exerciseTemplateRepository.getAllExercisesTemplates().collect { list ->
                _uiState.update {
                    it.copy(
                        exercises = list,
                        availableGroups = list.map { e -> e.muscleGroup }.distinct()
                    )
                }
            }
        }
    }

    fun toggleFavorite(exercise: ExerciseTemplate) {
        viewModelScope.launch {
            val updated = exercise.copy(isFavorite = !exercise.isFavorite)
            exerciseTemplateRepository.updateExerciseTemplate(updated)
        }
    }
}

