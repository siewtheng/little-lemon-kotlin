package com.siewtheng.littlelemon.composables

import com.siewtheng.littlelemon.R
import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


class Onboarding : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingScreen()
        }
    }
}

@Composable
fun OnboardingScreen() {
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
        UserInputForm()
        RegisterButton()
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
fun UserInputForm() {
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }

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
fun RegisterButton() {
    Button(
        onClick = { /* Implement registration logic here */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text("Register")
    }
}
