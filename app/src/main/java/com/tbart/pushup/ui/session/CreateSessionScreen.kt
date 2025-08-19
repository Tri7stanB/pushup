package com.tbart.pushup.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(
    viewModel: SessionViewModel = viewModel(),
    onSessionCreated: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Exercices :", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))

            var selectedExercise by remember { mutableStateOf("") }
            val availableExercises = listOf("Pompes", "Squats", "Tractions", "Abdos", "Fentes")
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedExercise,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Choisir un exercice") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableExercises.forEach { exercise ->
                        DropdownMenuItem(
                            text = { Text(exercise) },
                            onClick = {
                                selectedExercise = exercise
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

// Bouton ajouter
            Button(
                onClick = {
                    if (selectedExercise.isNotEmpty()) {
                        viewModel.addExerciseToSession(selectedExercise)
                        selectedExercise = ""
                    }
                },
                enabled = selectedExercise.isNotEmpty()
            ) {
                Text("Ajouter à la séance")
            }

            Spacer(Modifier.height(16.dp))
            Text("Créer une nouvelle séance", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            Button(onClick = {
                viewModel.createNewSession()
                uiState.sessionId?.let { onSessionCreated(it) }
            }) {
                Text("Commencer")
            }
            uiState.errorMessage?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
