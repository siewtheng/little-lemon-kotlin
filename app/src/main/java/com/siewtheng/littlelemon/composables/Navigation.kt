package com.siewtheng.littlelemon.composables

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val isLoggedIn = checkIfUserIsLoggedIn(context)

    NavHost(navController = navController,
        startDestination = if (isLoggedIn) HomeDestination.route else OnboardingDestination.route) {
        composable(OnboardingDestination.route) {
            OnboardingScreen(navController)
        }
        composable(HomeDestination.route) {
            HomeScreen()
        }
        composable(ProfileDestination.route) {
            ProfileScreen()
        }
    }

}

fun checkIfUserIsLoggedIn(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.contains("email")
}