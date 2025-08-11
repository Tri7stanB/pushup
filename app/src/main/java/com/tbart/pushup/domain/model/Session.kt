package com.tbart.pushup.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDateTime,
    val title: String
)
