package com.tbart.pushup.ui.home

import android.R
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tbart.pushup.ui.theme.PushUpTheme
import com.tbart.pushup.data.repository.SessionRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    sessionRepository: SessionRepository,
    onStartNewSession: (Int) -> Unit, // Passe l'ID de session créée vers CreateSession
    onResumeSession: (Int) -> Unit
) {
    // Utilisez un ViewModel qui peut créer des sessions
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(sessionRepository)
    )

    val uiState by viewModel.uiState.collectAsState()

    // Observez les changements d'état pour navigation vers CreateSession
    uiState.newSessionId?.let { sessionId ->
        onStartNewSession(sessionId)
        viewModel.onNavigationHandled() // Reset l'état après navigation
    }

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
            onStartNewSession = { viewModel.createNewSession() }, // Créer la session ici
            onResumeSession = { onResumeSession(uiState.activeSessionId ?: 0) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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

        Text(
            text = liveDateText(),
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

        // Gestion des erreurs
        uiState.errorMessage?.let { error ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(
                    onClick = onStartNewSession,
                    shape = RoundedCornerShape(12.dp), // coins arrondis personnalisés
                    modifier = Modifier
                        .padding(32.dp)
                        .size(height = 45.dp, width = 320.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text("Commencer ma séance")
                    }
                }
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

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun liveDateText(): String {
    var currentDateTime by remember { mutableStateOf(LocalDateTime.now()) }

    // Met à jour toutes les secondes (tu peux ajuster la fréquence)
    LaunchedEffect(Unit) {
        while (true) {
            delay(100L)
            currentDateTime = LocalDateTime.now()
        }
    }

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.getDefault())
    val formattedDate = currentDateTime.format(formatter)

    return formattedDate
}