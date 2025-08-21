package com.example.pushup

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tbart.pushup.navigation.NavRoutes

@Composable
fun BottomNavBar(
    navController: NavController,
    onNewSessionClick: () -> Unit
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        listOf(NavRoutes.Home, NavRoutes.CreateSession, NavRoutes.Agenda).forEach { route ->
            val selected = currentRoute?.startsWith(route.route.substringBefore("/{")) == true

            NavigationBarItem(
                icon = { Icon(route.icon, contentDescription = route.title) },
                label = { Text(route.title) },
                selected = selected,
                onClick = {
                    when (route) {
                        is NavRoutes.CreateSession -> onNewSessionClick()
                        else -> navController.navigate(route.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
