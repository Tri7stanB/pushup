package com.tbart.pushup.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_templates")
data class ExerciseTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val muscleGroup: String, // "Pectoraux", "Jambes", "Dos", etc.
    val imageRes: String? = null, // chemin local ou url vers l’illustration
    val isFavorite: Boolean = false
)
