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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.siewtheng.littlelemon.R
import com.siewtheng.littlelemon.ui.theme.highlightWhite
import com.siewtheng.littlelemon.ui.theme.primaryGreen
import com.siewtheng.littlelemon.ui.theme.secondaryLightOrange
import com.siewtheng.littlelemon.ui.theme.secondaryOrange

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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(userData: UserData, onSave: (UserData) -> Unit, onBack: () -> Unit, onLogout: () -> Unit) {
    var firstName by remember { mutableStateOf(userData.firstName) }
    var lastName by remember { mutableStateOf(userData.lastName) }
    var email by remember { mutableStateOf(userData.email) }
    var showAlert by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

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
            style = MaterialTheme.typography.displayMedium.copy(
                color = secondaryOrange,
            ),
            textAlign = TextAlign.Center
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

        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = primaryGreen
                ),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = primaryGreen,
                    focusedLabelColor = primaryGreen,
                    unfocusedLabelColor = primaryGreen,
                    focusedIndicatorColor = primaryGreen,
                    unfocusedIndicatorColor = primaryGreen
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
                visualTransformation = VisualTransformation.None,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = primaryGreen
                ),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = primaryGreen,
                    focusedLabelColor = primaryGreen,
                    unfocusedLabelColor = primaryGreen,
                    focusedIndicatorColor = primaryGreen,
                    unfocusedIndicatorColor = primaryGreen
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
                visualTransformation = VisualTransformation.None,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = primaryGreen
                ),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = primaryGreen,
                    focusedLabelColor = primaryGreen,
                    unfocusedLabelColor = primaryGreen,
                    focusedIndicatorColor = primaryGreen,
                    unfocusedIndicatorColor = primaryGreen
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
                visualTransformation = VisualTransformation.None,
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                onSave(UserData(firstName, lastName, email))
                showAlert = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryGreen,
                contentColor = highlightWhite
            ),
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
                    Button(onClick = { showAlert = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryGreen,
                            contentColor = highlightWhite
                        )) {
                        Text("OK")
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack,
            colors = ButtonDefaults.buttonColors(
            containerColor = primaryGreen,
            contentColor = highlightWhite
        ),
            modifier = Modifier.fillMaxWidth()
        ) {
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
