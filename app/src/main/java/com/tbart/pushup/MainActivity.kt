package com.tbart.pushup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tbart.pushup.navigation.NavRoutes
import com.tbart.pushup.ui.screens.home.HomeScreen
import com.tbart.pushup.ui.session.CreateSessionScreen
import com.tbart.pushup.ui.session.SessionDetailsScreen
import com.tbart.pushup.ui.theme.PushUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PushUpTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Home.route
                    ) {
                        composable(NavRoutes.Home.route) {
                            HomeScreen(
                                onStartNewSession = {
                                    navController.navigate(NavRoutes.CreateSession.route)
                                },
                                onResumeSession = { sessionId ->
                                    navController.navigate(NavRoutes.SessionDetails.createRoute(sessionId))
                                }
                            )
                        }
                        composable(NavRoutes.CreateSession.route) {
                            CreateSessionScreen(
                                onSessionCreated = { sessionId ->
                                    navController.navigate(NavRoutes.SessionDetails.createRoute(sessionId))
                                }
                            )
                        }
                        composable(
                            route = NavRoutes.SessionDetails.route,
                            arguments = listOf(
                                navArgument("sessionId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: 0
                            SessionDetailsScreen(sessionId = sessionId)
                        }
                    }
                }
            }
        }
    }
}
