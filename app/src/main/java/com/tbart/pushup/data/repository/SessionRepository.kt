package com.tbart.pushup.data.repository


import android.os.Build
import androidx.annotation.RequiresApi
import com.tbart.pushup.data.local.SessionDao
import com.tbart.pushup.domain.model.Session
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class SessionRepository(
    private val sessionDao: SessionDao
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getUpcomingSessions(): Flow<List<Session>> =
        sessionDao.getUpcomingSessions(LocalDateTime.now())

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPastSessions(): Flow<List<Session>> =
        sessionDao.getPastSessions(LocalDateTime.now())

    suspend fun addSession(session: Session) =
        sessionDao.insertSession(session)

    suspend fun deleteSession(session: Session) =
        sessionDao.deleteSession(session)
}
