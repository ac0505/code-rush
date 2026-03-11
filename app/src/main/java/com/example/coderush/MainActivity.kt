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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.ui.theme.JockeyOne
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onSinglePlayerClick = { openDifficultyScreen("single") },
                onMultiPlayerClick = { openMultiPlayerFlow() },
                onLeaderboardClick = { openLeaderboard() },
                onHowToPlayClick = { openOptionScreen() },
                onCreditsClick = { openCreditScreen() },
                onSettingsClick = { openOptionScreen() },
                onAccountClick = { openAccountScreen() }
            )
        }
    }

    private fun openDifficultyScreen(mode: String) {
        val intent = Intent(this, Difficulty::class.java)
        intent.putExtra("MODE", mode)
        startActivity(intent)
    }

    private fun openMultiPlayerFlow() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // Already logged in - go to difficulty
            val intent = Intent(this, Difficulty::class.java)
            intent.putExtra("MODE", "multi")
            intent.putExtra("USERNAME", auth.currentUser?.displayName ?: auth.currentUser?.email?.substringBefore("@") ?: "Player")
            startActivity(intent)
        } else {
            // Need to log in first
            startActivity(Intent(this, Account::class.java))
        }
    }

    private fun openLeaderboard() {
        val intent = Intent(this, Leaderboard::class.java)
        intent.putExtra("SOURCE", "menu") // from main menu, no score shown
        startActivity(intent)
    }

    private fun openOptionScreen() {
        startActivity(Intent(this, Options::class.java))
    }

    private fun openCreditScreen() {
        startActivity(Intent(this, Credits::class.java))
    }

    private fun openAccountScreen() {
        startActivity(Intent(this, Account::class.java))
    }
}


@Composable
fun MainScreen(
    onSinglePlayerClick: () -> Unit,
    onMultiPlayerClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onHowToPlayClick: () -> Unit,
    onCreditsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAccountClick: () -> Unit
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

        // Top-left Settings icon
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // Top-right Account Circle icon
        IconButton(
            onClick = onAccountClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Account",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

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

            Spacer(modifier = Modifier.height(16.dp))

            // Leaderboard button
            Button(
                onClick = onLeaderboardClick,
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
                    "LeaderBoard",
                    fontFamily = JockeyOne,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // How to Play and Credits buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 65.dp, end = 65.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onHowToPlayClick,
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("How to play", fontSize = 16.sp, fontFamily = JockeyOne)
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
