package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.presentation.navigation.Screen
import androidx.navigation.NavHostController

@Composable
fun AuthLoginScreen(navController: NavHostController) {
    val emailState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Log in or sign up",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Create an account or log in to book and manage your appointments",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )

        SocialButton(text = "Continue with Facebook")
        SocialButton(text = "Continue with Google")

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("OR", color = Color.Gray)
        }

        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email address") }
        )

        Button(
            onClick = { navController.navigate(Screen.AuthRegister) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Continue", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Have a business account?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Sign in as a professional",
            color = Color(0xFF6C47FF),
            modifier = Modifier.clickable { }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Successfully logged out",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x11000000), shape = RoundedCornerShape(12.dp))
                .padding(12.dp),
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Composable
fun CreateAccountScreen(navController: NavHostController) {
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val agreePolicy = remember { mutableStateOf(false) }
    val agreeMarketing = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Create account",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "You're almost there! Complete these details",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )

        OutlinedTextField(value = firstName.value, onValueChange = { firstName.value = it }, label = { Text("First name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = lastName.value, onValueChange = { lastName.value = it }, label = { Text("Last name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password.value, onValueChange = { password.value = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = phone.value, onValueChange = { phone.value = it }, label = { Text("Mobile number") }, modifier = Modifier.fillMaxWidth())

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreePolicy.value, onCheckedChange = { agreePolicy.value = it })
            Text(text = "I agree to the Privacy Policy, Terms of Use and Terms of Service")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = agreeMarketing.value, onCheckedChange = { agreeMarketing.value = it })
            Text(text = "I agree to receive marketing notifications with offers and news")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO: trigger sign up */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = agreePolicy.value,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Create account", color = Color.White)
        }
    }
}

@Composable
private fun SocialButton(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text, fontSize = 16.sp)
        }
    }
}