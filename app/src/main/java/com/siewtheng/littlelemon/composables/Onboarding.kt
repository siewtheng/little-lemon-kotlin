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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.siewtheng.littlelemon.ui.theme.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController


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
            text = "Welcome to Little Lemon Restaurant !!!",
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            style = MaterialTheme.typography.displayMedium.copy(
                color = secondaryOrange,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp
            ),
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(R.drawable.restaurant),
            contentDescription = "Restaurant Picture",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
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
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun UserInputForm(firstName: MutableState<String>, lastName: MutableState<String>, email: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = firstName.value,
            onValueChange = { firstName.value = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = primaryGreen,
                focusedLabelColor = primaryGreen,
                unfocusedLabelColor = primaryGreen,
                focusedIndicatorColor = primaryGreen,
                unfocusedIndicatorColor = primaryGreen
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = primaryGreen
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
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = lastName.value,
            onValueChange = { lastName.value = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = primaryGreen,
                focusedLabelColor = primaryGreen,
                unfocusedLabelColor = primaryGreen,
                focusedIndicatorColor = primaryGreen,
                unfocusedIndicatorColor = primaryGreen
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = primaryGreen
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
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = primaryGreen,
                focusedLabelColor = primaryGreen,
                unfocusedLabelColor = primaryGreen,
                focusedIndicatorColor = primaryGreen,
                unfocusedIndicatorColor = primaryGreen
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            visualTransformation = VisualTransformation.None,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = primaryGreen
            )
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
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryGreen,
            contentColor = highlightWhite
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

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