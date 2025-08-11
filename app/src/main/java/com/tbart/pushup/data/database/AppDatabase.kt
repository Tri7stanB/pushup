package com.tbart.pushup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tbart.pushup.data.local.Converters
import com.tbart.pushup.data.local.ExerciseDao
import com.tbart.pushup.data.local.SessionDao
import com.tbart.pushup.domain.model.Exercise
import com.tbart.pushup.domain.model.Session


@Database(
    entities = [Session::class, Exercise::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun exerciseDao(): ExerciseDao
}
