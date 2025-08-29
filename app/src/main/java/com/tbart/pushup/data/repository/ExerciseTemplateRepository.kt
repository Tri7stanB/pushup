package com.tbart.pushup.data.repository

import com.tbart.pushup.data.local.ExerciseTemplateDao
import com.tbart.pushup.domain.model.ExerciseTemplate
import kotlinx.coroutines.flow.Flow

class ExerciseTemplateRepository(
    private val templateDao: ExerciseTemplateDao
) {
    fun getAllExercisesTemplates(): Flow<List<ExerciseTemplate>> =
        templateDao.getAll()

    fun getFavoriteTemplates(): Flow<List<ExerciseTemplate>> =
        templateDao.getFavorites()

    suspend fun insertExerciseTemplate(template: ExerciseTemplate) {
        templateDao.insert(template)
    }

    suspend fun updateExerciseTemplate(template: ExerciseTemplate) {
        templateDao.update(template)
    }

    suspend fun insertAll(templates: List<ExerciseTemplate>) {
        templateDao.insertAll(templates)
    }
}
