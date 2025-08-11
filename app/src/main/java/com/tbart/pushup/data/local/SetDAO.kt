package com.tbart.pushup.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tbart.pushup.domain.model.Set
import kotlinx.coroutines.flow.Flow

@Dao
interface  SetDAO {

    @Query("SELECT * FROM sets WHERE sessionId = :sessionId")
    fun getSetsBySession(sessionId: Int): Flow<List<Set>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: Set)

    @Delete
    suspend fun deleteSet(set: Set)
}