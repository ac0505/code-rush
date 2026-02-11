package com.example.coderush.singleplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import com.example.coderush.Difficulty
import com.example.coderush.MainActivity
import com.example.coderush.R
import com.example.coderush.ui.theme.Jersey20

class SingleScore : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the score passed from Easy.kt
        val score = intent.getIntExtra("SCORE", 0)

        setContent {
            ScoreScreen(
                score,
                onPlayClick = { openDifficultyScreen() },
                onMenuClick = { openMainScreen() }
            )
        }
    }

    private fun openDifficultyScreen() {
        val intent = Intent(this, Difficulty::class.java)
        startActivity(intent)
    }

    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun ScoreScreen(
    score: Int,
    onPlayClick: () -> Unit,
    onMenuClick: () -> Unit) {
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
            // First text
            Text(
                "MY SCORE:",
                fontSize = 60.sp,
                fontFamily = Jersey20,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Second text showing actual score
            Text(
                "$score",
                fontSize = 48.sp,
                fontFamily = Jersey20,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(200.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .width(180.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("PLAY AGAIN", fontSize = 26.sp, fontFamily = Jersey20)
                }

                Button(
                    onClick = onMenuClick,
                    modifier = Modifier
                        .width(180.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("MAIN MENU", fontSize = 26.sp, fontFamily = Jersey20)
                }
            }
        }
    }
}
