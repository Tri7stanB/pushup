package com.tbart.pushup.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tbart.pushup.data.repository.SessionRepository
import com.tbart.pushup.domain.model.Exercise
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailsScreen(
    sessionId: Int,
    sessionRepository: SessionRepository
) {
    val viewModel: SessionViewModel = viewModel(
        factory = SessionViewModelFactory(sessionRepository)
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sessionId) {
        viewModel.loadSessionDetails(sessionId)
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Séance #${uiState.sessionId} - ${uiState.title}", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("Date : ${uiState.date}")
            Spacer(Modifier.height(16.dp))

            Text("Exercices :", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                items(uiState.exercises) { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        onRepetitionsChange = { newReps ->
                            viewModel.updateExerciseRepetitions(exercise.id, newReps)
                        }
                    )
                }
            }
            uiState.errorMessage?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }

}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onRepetitionsChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = exercise.name,
            modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
            value = exercise.repetitions.toString(),
            onValueChange = { newValue ->
                val intValue = newValue.toIntOrNull() ?: 0
                onRepetitionsChange(intValue)
            },
            modifier = Modifier.width(80.dp),
            singleLine = true,
            label = { Text("Reps") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
