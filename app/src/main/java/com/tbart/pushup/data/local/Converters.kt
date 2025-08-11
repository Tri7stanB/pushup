package com.tbart.pushup.data.local

//Note sur les dates : Room ne sait pas stocker LocalDateTime directement.
//On va créer un TypeConverter.

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}