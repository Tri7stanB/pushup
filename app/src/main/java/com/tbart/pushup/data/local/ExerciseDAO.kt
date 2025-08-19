package com.tbart.pushup.data.local

import androidx.room.*
import com.tbart.pushup.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercises WHERE sessionId = :sessionId")
    fun getExercisesBySession(sessionId: Int): Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises WHERE sessionId = :sessionId")
    suspend fun getExercisesBySessionSync(sessionId: Int): List<Exercise>
}
