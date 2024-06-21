package com.siewtheng.littlelemon.composables

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class Home : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "homeScreen") {
                composable("homeScreen") {
                    HomeScreen(navController)
                }
                composable(ProfileDestination.route) {
                    Profile(navController) // Assuming Profile is set up properly to handle navController
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Text("Welcome to the Home Screen")
    Button(onClick = { navController.navigate(ProfileDestination.route) }) {
        Text("Go to Profile")
    }
}