package com.siewtheng.littlelemon.composables

import com.siewtheng.littlelemon.R
import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.runtime.MutableState

class Onboarding : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "onboarding") {
                        composable("onboarding") {
                            OnboardingScreen(navController)
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen(navController: NavHostController) {
    val context = LocalContext.current
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val registrationMessage = remember { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Header()
        Text(
            text = "Welcome to Little Lemon Restaurant!",
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Image(
            painter = painterResource(R.drawable.restaurant),
            contentDescription = "Restaurant Picture",
            modifier = Modifier.fillMaxWidth()
        )
        UserInputForm(firstName, lastName, email)
        RegisterButton(firstName, lastName, email, registrationMessage, context, navController)
        registrationMessage.value?.let {
            Text(
                text = it,
                color = if (it.contains("unsuccessful")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun Header() {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "App Logo",
        modifier = Modifier.fillMaxWidth().height(50.dp)
    )
}

@Composable
fun UserInputForm(firstName: MutableState<String>, lastName: MutableState<String>, email: MutableState<String>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("First Name", modifier = Modifier.padding(bottom = 1.dp))
        OutlinedTextField(
            value = firstName.value,
            onValueChange = { firstName.value = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Last Name", modifier = Modifier.padding(top = 2.dp, bottom = 1.dp))
        OutlinedTextField(
            value = lastName.value,
            onValueChange = { lastName.value = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Text("Email Address", modifier = Modifier.padding(top = 2.dp, bottom = 1.dp))
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email Address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun RegisterButton(firstName: MutableState<String>, lastName: MutableState<String>, email: MutableState<String>, registrationMessage: MutableState<String?>, context: Context, navController: NavHostController) {
    Button(
        onClick = {
            if (firstName.value.isBlank() || lastName.value.isBlank() || email.value.isBlank()) {
                registrationMessage.value = "Registration unsuccessful. Please enter all data."
            } else {
                saveRegistrationData(context, firstName.value, lastName.value, email.value)
                registrationMessage.value = "Registration successful!"
                navController.navigate("home")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text("Register")
    }
}

fun saveRegistrationData(context: Context, firstName: String, lastName: String, email: String) {
    val sharedPref = context.getSharedPreferences("AppData", Context.MODE_PRIVATE) ?: return
    with (sharedPref.edit()) {
        putString("FirstName", firstName)
        putString("LastName", lastName)
        putString("Email", email)
        apply()
    }
}