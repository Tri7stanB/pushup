package com.tbart.pushup.utils

import android.content.Context
import com.tbart.pushup.domain.model.ExerciseTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

suspend fun loadExercisesFromJson(context: Context): List<ExerciseTemplate> = withContext(Dispatchers.IO) {
    val jsonString = context.assets.open("exercises.json")
        .bufferedReader()
        .use { it.readText() }

    val jsonArray = JSONArray(jsonString)
    val list = mutableListOf<ExerciseTemplate>()
    for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        list.add(
            ExerciseTemplate(
                name = obj.getString("name"),
                muscleGroup = obj.getString("bodyPart"),
                imageRes = obj.getString("imageRes")
            )
        )
    }
    list
}
