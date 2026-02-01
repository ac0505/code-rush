package com.example.coderush

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.*
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                onSinglePlayerClick = { openDifficultyScreen() },
                onMultiPlayerClick = { openDifficultyScreen() },
                onOptionsClick = { openOptionScreen() },
                onCreditsClick = { openCreditScreen() }
            )
        }
    }

    private fun openDifficultyScreen() {
        val intent = Intent(this, Difficulty::class.java)
        startActivity(intent)
    }

    private fun openOptionScreen() {
        val intent = Intent(this, Options::class.java)
        startActivity(intent)
    }

    private fun openCreditScreen() {
        val intent = Intent(this, Credits::class.java)
        startActivity(intent)
    }
}

@Composable
fun MainScreen(
    onSinglePlayerClick: () -> Unit,
    onMultiPlayerClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onCreditsClick: () -> Unit
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
            Image(
                painter = painterResource(id = R.drawable.game_name),
                contentDescription = "Game Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start=48.dp, end=48.dp, bottom=48.dp)
            )
            Button(
                onClick = onSinglePlayerClick,
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
                    "Single Player",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start=12.dp, end=12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onMultiPlayerClick,
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
                    "Multi Player",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start=12.dp, end=12.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 65.dp, end = 65.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onOptionsClick,
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("Options", fontSize = 16.sp)
                }

                Button(
                    onClick = onCreditsClick,
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("Credits", fontSize = 16.sp)
                }
            }
        }
    }
}
