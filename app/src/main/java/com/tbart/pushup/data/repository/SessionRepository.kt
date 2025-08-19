package com.tbart.pushup.data.repository


import android.os.Build
import androidx.annotation.RequiresApi
import com.tbart.pushup.data.local.ExerciseDao
import com.tbart.pushup.data.local.SessionDao
import com.tbart.pushup.domain.model.Exercise
import com.tbart.pushup.domain.model.Session
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class SessionRepository(
    private val sessionDao: SessionDao,
    private val exerciseDao: ExerciseDao
) {
    // --- Sessions ---
    @RequiresApi(Build.VERSION_CODES.O)
    fun getUpcomingSessions(): Flow<List<Session>> =
        sessionDao.getUpcomingSessions(LocalDateTime.now())

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPastSessions(): Flow<List<Session>> =
        sessionDao.getPastSessions(LocalDateTime.now())

    suspend fun addSession(session: Session): Long {
        sessionDao.insertSession(session)
        return session.id.toLong()
    }

    suspend fun deleteSession(session: Session) =
        sessionDao.deleteSession(session)

    // --- Exercices ---
    fun getExercises(sessionId: Int): Flow<List<Exercise>> =
        exerciseDao.getExercisesBySession(sessionId)

    suspend fun addExercise(exercise: Exercise) =
        exerciseDao.insertExercise(exercise)

    suspend fun deleteExercise(exercise: Exercise) =
        exerciseDao.deleteExercise(exercise)
}

