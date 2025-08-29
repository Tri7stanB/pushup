package com.tbart.pushup.data.local

import androidx.room.*
import com.tbart.pushup.domain.model.ExerciseTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTemplateDao {
    @Query("SELECT * FROM exercise_templates")
    fun getAll(): Flow<List<ExerciseTemplate>>

    @Query("SELECT * FROM exercise_templates WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<ExerciseTemplate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(template: ExerciseTemplate)

    @Update
    suspend fun update(template: ExerciseTemplate)

    @Delete
    suspend fun delete(template: ExerciseTemplate)

    @Insert
    suspend fun insertAll(templates: List<ExerciseTemplate>) {
        templates.forEach { insert(it) }
    }

}
