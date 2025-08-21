package com.tbart.pushup.ui.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tbart.pushup.data.repository.ExerciseTemplateRepository

class ExercisesViewModelFactory(
    private val repository: ExerciseTemplateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExercisesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
