package com.tbart.pushup.data.local

import androidx.room.*
import com.tbart.pushup.domain.model.Session
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface SessionDao {

    @Query("SELECT * FROM sessions WHERE date >= :now ORDER BY date ASC")
    fun getUpcomingSessions(now: LocalDateTime): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE date < :now ORDER BY date DESC")
    fun getPastSessions(now: LocalDateTime): Flow<List<Session>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session) : Long

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: Int): Session?
}