package com.practice.weather.view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practice.weather.viewmodel.AppViewModel

@Composable
fun AppNavigation(viewModel: AppViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, viewModel) }
        composable("weather/{city}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("city") ?: ""
            DetailsScreen(navController, viewModel, cityName)
        }
    }
}
