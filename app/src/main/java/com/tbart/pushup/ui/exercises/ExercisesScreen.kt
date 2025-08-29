package com.tbart.pushup.ui.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tbart.pushup.domain.model.ExerciseTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    viewModel: ExercisesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // ⚡ Charger les données une seule fois
    LaunchedEffect(Unit) {
        viewModel.initData(context)
    }

    val groups = listOf("Tous") + uiState.availableGroups
    var selectedGroup by remember { mutableStateOf("Tous") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Catalogue d'exercices", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        // 🔹 Filtre par groupe musculaire
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groups.forEach { group ->
                FilterChip(
                    selected = selectedGroup == group,
                    onClick = { selectedGroup = group },
                    label = { Text(group) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🔹 Liste filtrée
        val filtered = if (selectedGroup == "Tous") uiState.exercises
        else uiState.exercises.filter { it.muscleGroup == selectedGroup }

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Aucun exercice disponible")
            }
        } else {
            LazyColumn {
                items(filtered) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onFavoriteToggle = { viewModel.toggleFavorite(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseTemplate,
    onFavoriteToggle: (ExerciseTemplate) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = exercise.name,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(exercise.name, style = MaterialTheme.typography.titleMedium)
                    Text(exercise.muscleGroup, style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = { onFavoriteToggle(exercise) }) {
                Icon(
                    imageVector = if (exercise.isFavorite) Icons.Default.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favori",
                    tint = if (exercise.isFavorite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
