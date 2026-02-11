package com.example.coderush.multiplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.R
import com.example.coderush.ui.theme.JockeyOne

class MultiCreate : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateAccountScreen(
                onCreateClick = { openLoginScreen() }
            )
        }
    }

    private fun openLoginScreen() {
        val intent = Intent(this, MultiLogin::class.java)
        startActivity(intent)
    }
}

@Composable
fun CreateAccountScreen(
    onCreateClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        // Background Image
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.code_rush_logo),
                contentDescription = "Game Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentScale = ContentScale.Fit
            )

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Username", color = Color(0xFF003B8E), fontFamily = JockeyOne) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF003B8E),
                    unfocusedTextColor = Color(0xFF003B8E),
                    cursorColor = Color(0xFF003B8E),
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedContainerColor = Color.White.copy(alpha = 0.2f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = Color(0xFF003B8E), fontFamily = JockeyOne) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF003B8E),
                    unfocusedTextColor = Color(0xFF003B8E),
                    cursorColor = Color(0xFF003B8E),
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedContainerColor = Color.White.copy(alpha = 0.2f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirm Password", color = Color(0xFF003B8E), fontFamily = JockeyOne) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF003B8E),
                    unfocusedTextColor = Color(0xFF003B8E),
                    cursorColor = Color(0xFF003B8E),
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedContainerColor = Color.White.copy(alpha = 0.2f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Create Account Button
            Button(
                onClick = onCreateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A47A3)
                ),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Text(
                    text = "CREATE ACCOUNT",
                    fontFamily = JockeyOne,
                    color = Color.White,
                    fontSize = 26.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Already have an account? Login",
                fontSize = 12.sp,
                color = Color(0xFF003B8E),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    // Navigate back to login
                    onCreateClick()
                }
            )
        }
    }
}
