package com.example.coderush

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.*

class Difficulty : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DifficultyScreen(
                onEasyClick = { openGameScreen1() },
                onNormalClick = {},
                onHardClick = {}
            )
        }
    }

    private fun openGameScreen1() {
        val intent = Intent(this, Easy::class.java)
        startActivity(intent)
    }
}

@Composable
fun DifficultyScreen(
    onEasyClick: () -> Unit,
    onNormalClick: () -> Unit,
    onHardClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.game_bg), // replace with your background drawable
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // makes the image fill the screen nicely
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select Difficulty:",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onEasyClick,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .height(70.dp)
                    .width(220.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7))
            ) {
                Text(
                    "Easy Mode",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNormalClick,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .height(70.dp)
                    .width(220.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7))
            ) {
                Text(
                    "Normal Mode",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onHardClick,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .height(70.dp)
                    .width(220.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7))
            ) {
                Text(
                    "Hard Mode",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
            }
        }
    }
}
