package com.tbart.pushup.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object CreateSession : NavRoutes("create_session")
    object SessionDetails : NavRoutes("session_details/{sessionId}") {
        fun createRoute(sessionId: Int) = "session_details/$sessionId"
    }
}
