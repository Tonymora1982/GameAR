package com.canchas.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.canchas.app.presentation.auth.AuthScreen
import com.canchas.app.presentation.auth.AuthViewModel
import com.canchas.app.presentation.calendar.ReservationCalendarScreen
import com.canchas.app.presentation.map.VenueMapScreen

@Composable
fun AppNavGraph(isAuthenticated: Boolean) {
    val nav = rememberNavController()
    val start = if (isAuthenticated) "map" else "auth"

    NavHost(navController = nav, startDestination = start) {
        composable("auth") { AuthScreen() }
        composable("map") {
            VenueMapScreen()
        }
        composable(
            route = "calendar/{venueId}",
            arguments = listOf(navArgument("venueId") { type = NavType.StringType })
        ) { backStack ->
            val venueId = backStack.arguments?.getString("venueId") ?: return@composable
            ReservationCalendarScreen(venueId = venueId, onBack = { nav.popBackStack() })
        }
    }
}