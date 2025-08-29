package com.tbart.pushup.ui.exercises

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbart.pushup.data.repository.ExerciseTemplateRepository
import com.tbart.pushup.domain.model.ExerciseTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

data class ExercisesUiState(
    val exercises: List<ExerciseTemplate> = emptyList(),
    val availableGroups: List<String> = emptyList()
)

class ExercisesViewModel(
    private val exerciseTemplateRepository: ExerciseTemplateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    fun initData(context: Context) {
        viewModelScope.launch {
            val current = exerciseTemplateRepository.getAllExercisesTemplates().first()
            if (current.isEmpty()) {
                val loaded = loadExercisesFromJson(context)
                exerciseTemplateRepository.insertAll(loaded)
            }
            loadExercises()
        }
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

    private suspend fun loadExercisesFromJson(context: Context): List<ExerciseTemplate> =
        withContext(Dispatchers.IO) {
            val jsonString = context.assets.open("exercises.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<ExerciseTemplate>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    ExerciseTemplate(
                        name = obj.getString("name"),
                        muscleGroup = obj.getString("bodyPart"),
                        imageRes = obj.optString("imageRes", ""),
                        isFavorite = false
                    )
                )
            }
            list
        }
}
