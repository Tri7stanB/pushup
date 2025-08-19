package com.tbart.pushup.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tbart.pushup.data.repository.SessionRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(
    sessionId: Int, // Reçoit l'ID de session créée depuis HomeScreen
    onSessionCreated: (Int) -> Unit,
    sessionRepository: SessionRepository
) {
    val viewModel: SessionViewModel = viewModel(
        factory = SessionViewModelFactory(sessionRepository)
    )

    val uiState by viewModel.uiState.collectAsState()

    // Initialiser le ViewModel avec l'ID de session existant
    LaunchedEffect(sessionId) {
        viewModel.loadSession(sessionId)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Préparez votre séance",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(32.dp))

            // Afficher les exercices actuels de la session
            Text(
                "Exercices sélectionnés :",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            if (uiState.exercises.isNotEmpty()) {
                uiState.exercises.forEach { exercise ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${exercise.name}")
                            Text(
                                "${exercise.repetitions} reps",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                Text(
                    "Aucun exercice ajouté pour le moment",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(24.dp))

            // Interface pour ajouter des exercices
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

            Spacer(Modifier.height(32.dp))

            // Boutons pour continuer
            Button(
                onClick = {
                    onSessionCreated(sessionId)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Commencer ma séance")
            }

            Spacer(Modifier.height(8.dp))

            if (uiState.exercises.isEmpty()) {
                OutlinedButton(
                    onClick = {
                        onSessionCreated(sessionId)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Commencer sans exercices")
                }
            }

            // Gestion des erreurs
            uiState.errorMessage?.let { error ->
                Spacer(Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}