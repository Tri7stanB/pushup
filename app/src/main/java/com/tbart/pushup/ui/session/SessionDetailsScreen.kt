package com.tbart.pushup.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SessionDetailsScreen(
    sessionId: Int,
    viewModel: SessionViewModel = viewModel()
) {
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
            Text(uiState.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("Date : ${uiState.date}")
            Spacer(Modifier.height(16.dp))
            Text("Exercices :", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            uiState.exercises.forEach { exercise ->
                Text("- $exercise", style = MaterialTheme.typography.bodyLarge)
            }
            uiState.errorMessage?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
