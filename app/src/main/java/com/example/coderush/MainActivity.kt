package com.example.coderush

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
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
import com.example.coderush.multiplayer.MultiLogin
import com.example.coderush.ui.theme.JockeyOne

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onSinglePlayerClick = { openDifficultyScreen("single") },
                onMultiPlayerClick = { openMultiLogin("multi") },
                onOptionsClick = { openOptionScreen() },
                onCreditsClick = { openCreditScreen() }
            )
        }
    }

    // ---------- NAVIGATION FUNCTIONS ----------

    private fun openDifficultyScreen(mode: String) {
        val intent = Intent(this, Difficulty::class.java)
        intent.putExtra("MODE", mode) // Pass mode forward
        startActivity(intent)
    }

    private fun openMultiLogin(mode: String) {
        val intent = Intent(this, MultiLogin::class.java)
        intent.putExtra("MODE", mode) // Pass mode forward
        startActivity(intent)
    }

    private fun openOptionScreen() {
        startActivity(Intent(this, Options::class.java))
    }

    private fun openCreditScreen() {
        startActivity(Intent(this, Credits::class.java))
    }
}

// ---------- COMPOSABLE ----------

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
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Game name/logo
            Image(
                painter = painterResource(id = R.drawable.game_name),
                contentDescription = "Game Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, end = 48.dp, bottom = 48.dp)
            )

            // Single Player button
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
                    fontFamily = JockeyOne,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multi Player button
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
                    fontFamily = JockeyOne,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Options and Credits buttons
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
                    Text("Options", fontSize = 18.sp, fontFamily = JockeyOne)
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
                    Text("Credits", fontSize = 18.sp, fontFamily = JockeyOne)
                }
            }
        }
    }
}
