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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.R
import com.example.coderush.Difficulty
import com.example.coderush.ui.theme.JockeyOne

class MultiLogin : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen { enteredUsername ->
                openDifficultyScreen(enteredUsername)
            }
        }
    }

    private fun openDifficultyScreen(username: String) {
        val intent = Intent(this, Difficulty::class.java)
        intent.putExtra("MODE", "multi")
        intent.putExtra("USERNAME", username)
        startActivity(intent)
        finish()
    }
}

@Composable
fun LoginScreen(
    onLoginClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

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

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onLoginClick(username) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Text("LOGIN", color = Color.White, fontSize = 26.sp, fontFamily = JockeyOne)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Create an Account",
                fontSize = 12.sp,
                color = Color(0xFF003B8E),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, MultiCreate::class.java))
                }
            )
        }
    }
}
