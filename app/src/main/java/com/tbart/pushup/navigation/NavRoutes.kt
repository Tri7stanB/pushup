package com.tbart.pushup.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String, val title: String, val icon: ImageVector) {
    object Home : NavRoutes("home", "Accueil", Icons.Default.Home)
    object CreateSession : NavRoutes("create_session/{sessionId}", "Nouvelle", Icons.Default.Add) {
        fun createRoute(sessionId: Int): String = "create_session/$sessionId"
    }

    object SessionDetails : NavRoutes("session/{sessionId}", "Détails", Icons.Default.Info) {
        fun createRoute(sessionId: Int) = "session/$sessionId"
    }
    object Agenda : NavRoutes("agenda", "Agenda", Icons.Default.Info) {
        const val routeWithDate = "agenda/{date}"
        fun createRoute(date: String) = "agenda/$date"
    }
}
