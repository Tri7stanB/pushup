package com.tbart.pushup

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.pushup.BottomNavBar
import com.tbart.pushup.data.database.AppDatabase
import com.tbart.pushup.data.repository.SessionRepository
import com.tbart.pushup.navigation.NavRoutes
import com.tbart.pushup.ui.home.HomeScreen
import com.tbart.pushup.ui.session.CreateSessionScreen
import com.tbart.pushup.ui.session.SessionDetailsScreen
import com.tbart.pushup.domain.model.Session
import com.tbart.pushup.ui.session.SessionViewModel
import com.tbart.pushup.ui.session.SessionViewModelFactory
import com.tbart.pushup.ui.theme.PushUpTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instance DB
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "pushup-db"
        ).build()

        // Repository
        val sessionRepository = SessionRepository(
            db.sessionDao(),
            db.exerciseDao()
        )

        setContent {
            PushUpTheme {
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()

                Scaffold(
                    bottomBar = {
                        BottomNavBar(
                            navController = navController,
                            onNewSessionClick = {
                                scope.launch {
                                    val sessionId = sessionRepository.addSession(
                                        Session(
                                            title = "Nouvelle séance",
                                            date = System.currentTimeMillis()
                                        )
                                    )
                                    navController.navigate(NavRoutes.CreateSession.createRoute(sessionId))
                                }
                            }
                        )
                    }
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
                                onSessionCreated = { newId ->
                                    navController.navigate(NavRoutes.SessionDetails.createRoute(newId))
                                },
                                sessionRepository = sessionRepository
                            )

                        }
                        composable(NavRoutes.Agenda.route) {
                            // TODO : écran agenda
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