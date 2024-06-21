package com.siewtheng.littlelemon.composables

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.siewtheng.littlelemon.R

data class UserData(val firstName: String, val lastName: String, val email: String)

@Composable
fun Profile(navController: NavHostController) {
    val context = LocalContext.current
    val userData = remember { mutableStateOf(UserData("", "", "")) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        userData.value = loadUserData(context)
        isLoading.value = false
    }

    if (isLoading.value) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else {
        ProfileContent(
            userData = userData.value,
            onSave = { updatedUserData ->
                saveUserData(context, updatedUserData)
                userData.value = updatedUserData
            },
            onBack = {
                navController.popBackStack()
            },
            onLogout = {
                clearUserData(context)
                navController.navigate("onboarding") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun ProfileContent(userData: UserData, onSave: (UserData) -> Unit, onBack: () -> Unit, onLogout: () -> Unit) {
    var firstName by remember { mutableStateOf(userData.firstName) }
    var lastName by remember { mutableStateOf(userData.lastName) }
    var email by remember { mutableStateOf(userData.email) }
    var showAlert by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Personal Information",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                onSave(UserData(firstName, lastName, email))
                showAlert = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text(text = "Confirmation") },
                text = { Text("Saved successfully!") },
                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Home")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Log Out", color = Color.White)
        }
    }
}

fun saveUserData(context: Context, userData: UserData) {
    val sharedPref = context.getSharedPreferences("AppData", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("FirstName", userData.firstName)
        putString("LastName", userData.lastName)
        putString("Email", userData.email)
        apply()
    }
}

fun loadUserData(context: Context): UserData {
    val sharedPref = context.getSharedPreferences("AppData", Context.MODE_PRIVATE)
    return UserData(
        firstName = sharedPref.getString("FirstName", "")!!,
        lastName = sharedPref.getString("LastName", "")!!,
        email = sharedPref.getString("Email", "")!!
    )
}

fun clearUserData(context: Context) {
    val sharedPref = context.getSharedPreferences("AppData", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        clear()
        apply()
    }
}
