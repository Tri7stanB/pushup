package com.tbart.pushup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.tbart.pushup.data.database.AppDatabase
import com.tbart.pushup.data.repository.SessionRepository
import com.tbart.pushup.navigation.BottomNavBar
import com.tbart.pushup.navigation.NavRoutes
import com.tbart.pushup.ui.home.HomeScreen
import com.tbart.pushup.ui.session.CreateSessionScreen
import com.tbart.pushup.ui.session.SessionDetailsScreen
import com.tbart.pushup.ui.session.SessionViewModel
import com.tbart.pushup.ui.session.SessionViewModelFactory
import com.tbart.pushup.ui.theme.PushUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instance DB
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "pushup-db"
        ).build()

        // Repository (injecte tes DAO)
        val sessionRepository = SessionRepository(
            db.sessionDao(),
            db.exerciseDao()
        )

        setContent {
            PushUpTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(NavRoutes.Home.route) {
                            HomeScreen(
                                sessionRepository = sessionRepository,
                                onStartNewSession = { sessionId ->
                                    navController.navigate(NavRoutes.CreateSession.createRoute(sessionId))
                                },
                                onResumeSession = { sessionId ->
                                    navController.navigate(NavRoutes.SessionDetails.createRoute(sessionId))
                                }
                            )
                        }
                        composable(
                            route = NavRoutes.CreateSession.route,
                            arguments = listOf(
                                navArgument("sessionId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: 0
                            CreateSessionScreen(
                                sessionId = sessionId,
                                onSessionCreated = { sessionId ->
                                    navController.navigate(NavRoutes.SessionDetails.createRoute(sessionId))
                                },
                                sessionRepository = sessionRepository
                            )
                        }
                        composable(NavRoutes.Agenda.route) {
                            // TODO
                        }
                        composable(
                            route = NavRoutes.SessionDetails.route,
                            arguments = listOf(
                                navArgument("sessionId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: 0
                            SessionDetailsScreen(
                                sessionId = sessionId,
                                sessionRepository = sessionRepository
                            )
                        }
                    }
                }
            }
        }
    }
}