package com.tbart.pushup.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tbart.pushup.ui.theme.PushUpTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onStartNewSession: () -> Unit,
    onResumeSession: (Int) -> Unit
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
        HomeContent(
            uiState = uiState,
            onStartNewSession = onStartNewSession,
            onResumeSession = { onResumeSession(123) } // TODO remplacer 123 par vrai ID
        )
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onStartNewSession: () -> Unit,
    onResumeSession: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Prêt pour votre séance ?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Statistiques rapides
        if (uiState.totalSessions > 0) {
            StatsCard(
                totalSessions = uiState.totalSessions,
                lastSessionDate = uiState.lastSessionDate
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Boutons d'action
        if (uiState.hasActiveSession) {
            Button(
                onClick = onResumeSession,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text("Reprendre ma séance")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onStartNewSession,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text("Nouvelle séance")
            }
        } else {
            Button(
                onClick = onStartNewSession,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text("Commencer ma séance")
            }
        }
    }
}

@Composable
private fun StatsCard(
    totalSessions: Int,
    lastSessionDate: String?
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Statistiques",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$totalSessions séances terminées",
                style = MaterialTheme.typography.bodyMedium
            )

            lastSessionDate?.let { date ->
                Text(
                    text = "Dernière séance : $date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PushUpTheme {
        HomeContent(
            uiState = HomeUiState(
                totalSessions = 5,
                lastSessionDate = "Hier",
                hasActiveSession = false
            ),
            onStartNewSession = {},
            onResumeSession = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenWithActiveSessionPreview() {
    PushUpTheme {
        HomeContent(
            uiState = HomeUiState(
                totalSessions = 3,
                lastSessionDate = "Il y a 2 jours",
                hasActiveSession = true
            ),
            onStartNewSession = {},
            onResumeSession = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenFirstTimePreview() {
    PushUpTheme {
        HomeContent(
            uiState = HomeUiState(),
            onStartNewSession = {},
            onResumeSession = {}
        )
    }
}