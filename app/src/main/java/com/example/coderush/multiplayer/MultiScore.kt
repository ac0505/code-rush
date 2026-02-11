package com.example.coderush.multiplayer

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.MainActivity
import com.example.coderush.R
import com.example.coderush.ui.theme.Jersey20

class MultiScore : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get score and username from intent
        val score = intent.getIntExtra("SCORE", 0)
        val username = intent.getStringExtra("USERNAME") ?: "PLAYER"

        setContent {
            MultiScoreScreen(
                username = username,
                score = score,
                onPlayClick = { openDifficultyScreen(username) },
                onMenuClick = { openMainScreen() }
            )
        }
    }

    private fun openDifficultyScreen(username: String) {
        val intent = Intent(this, com.example.coderush.Difficulty::class.java)
        intent.putExtra("MODE", "multi")
        intent.putExtra("USERNAME", username) // pass username forward
        startActivity(intent)
        finish()
    }

    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun MultiScoreScreen(
    username: String,
    score: Int,
    onPlayClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background
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
            Text(
                "WINNER:",
                fontSize = 60.sp,
                fontFamily = Jersey20,
                textAlign = TextAlign.Start,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                username,
                fontSize = 48.sp,
                fontFamily = Jersey20,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "SCORE:",
                fontSize = 60.sp,
                fontFamily = Jersey20,
                textAlign = TextAlign.Start,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "$score",
                fontSize = 48.sp,
                fontFamily = Jersey20,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(120.dp))

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