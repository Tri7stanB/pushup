package com.tbart.pushup.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sets")
data class Set (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var sessionId: Int,
    var exerciseId: Int,
    var reps: Int,
    var weight: Int,
    var duration: Int, // in seconds
    var rest: Int // in seconds
)