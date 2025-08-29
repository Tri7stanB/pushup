package com.tbart.pushup

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pushup.BottomNavBar
import com.tbart.pushup.data.database.AppDatabase
import com.tbart.pushup.data.repository.ExerciseTemplateRepository
import com.tbart.pushup.data.repository.SessionRepository
import com.tbart.pushup.navigation.NavRoutes
import com.tbart.pushup.ui.home.HomeScreen
import com.tbart.pushup.ui.session.CreateSessionScreen
import com.tbart.pushup.ui.session.SessionDetailsScreen
import com.tbart.pushup.domain.model.Session
import com.tbart.pushup.ui.exercises.ExercisesScreen
import com.tbart.pushup.ui.exercises.ExercisesViewModel
import com.tbart.pushup.ui.exercises.ExercisesViewModelFactory
import com.tbart.pushup.ui.theme.PushUpTheme
import com.tbart.pushup.utils.loadExercisesFromJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instance unique DB avec callback
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "pushup-db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(dbSupport: SupportSQLiteDatabase) {
                    super.onCreate(dbSupport)
                    CoroutineScope(Dispatchers.IO).launch {
                        val exercises = loadExercisesFromJson(applicationContext)

                        // ⚡ Ici on ouvre une instance temporaire sur la même DB
                        val instance = Room.databaseBuilder(
                            applicationContext,
                            AppDatabase::class.java,
                            "pushup-db"
                        ).build()

                        instance.exerciseTemplateDao().insertAll(exercises)
                        instance.close()
                    }
                }
            })
            .build()

        // Repository
        val sessionRepository = SessionRepository(
            db.sessionDao(),
            db.exerciseDao()
        )

        val exerciseTemplateRepository = ExerciseTemplateRepository(
            db.exerciseTemplateDao()
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
                        composable(NavRoutes.Exercises.route) {
                            val exerciseTemplateRepository = ExerciseTemplateRepository(db.exerciseTemplateDao())
                            val viewModel: ExercisesViewModel = viewModel(
                                factory = ExercisesViewModelFactory(exerciseTemplateRepository)
                            )
                            ExercisesScreen(viewModel = viewModel)
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