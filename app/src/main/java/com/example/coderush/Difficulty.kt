    package com.example.coderush

    import android.content.Intent
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.example.coderush.modes.Easy
    import com.example.coderush.modes.Hard
    import com.example.coderush.modes.Normal
    import com.example.coderush.ui.theme.Jersey20
    import com.example.coderush.ui.theme.JockeyOne

    class Difficulty : ComponentActivity() {

        private lateinit var mode: String
        private lateinit var username: String

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Get mode from MainActivity or MultiLogin
            mode = intent.getStringExtra("MODE") ?: "single"
            username = intent.getStringExtra("USERNAME") ?: "PLAYER"

            setContent {
                DifficultyScreen(
                    onEasyClick = { seconds ->
                        val intent = Intent(this, Easy::class.java)
                        intent.putExtra("MODE", mode)      // single/multi
                        intent.putExtra("TIME", seconds)
                        intent.putExtra("USERNAME", username)
                        startActivity(intent)
                        finish()
                    },
                    onNormalClick = { seconds ->
                        val intent = Intent(this, Normal::class.java)
                        intent.putExtra("MODE", mode)      // single/multi
                        intent.putExtra("TIME", seconds)
                        intent.putExtra("USERNAME", username)
                        startActivity(intent)
                        finish()
                    },
                    onHardClick = { seconds ->
                        val intent = Intent(this, Normal::class.java)
                        intent.putExtra("MODE", mode)      // single/multi
                        intent.putExtra("TIME", seconds)
                        intent.putExtra("USERNAME", username)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }

    @Composable
    fun DifficultyScreen(
        onEasyClick: (Int) -> Unit,
        onNormalClick: (Int) -> Unit,
        onHardClick: (Int) -> Unit
    ) {
        var showTimePopup by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxSize()) {
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
                Text(
                    "Select Difficulty:",
                    fontFamily = Jersey20,
                    fontSize = 52.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(40.dp))

                // EASY BUTTON
                Button(
                    onClick = { showTimePopup = true }, // show popup
                    modifier = Modifier
                        .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                        .height(70.dp)
                        .width(220.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7))
                ) {
                    Text("Easy Mode", fontSize = 30.sp, fontFamily = JockeyOne)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // NORMAL BUTTON
                Button(
                    onClick = { showTimePopup = true }, // show popup
                    modifier = Modifier
                        .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                        .height(70.dp)
                        .width(220.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7))
                ) {
                    Text("Normal Mode", fontSize = 30.sp, fontFamily = JockeyOne)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // HARD BUTTON
                Button(
                    onClick = { showTimePopup = true }, // show popup
                    modifier = Modifier
                        .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                        .height(70.dp)
                        .width(220.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7))
                ) {
                    Text("Hard Mode", fontSize = 30.sp, fontFamily = JockeyOne)
                }
            }
            if (showTimePopup) {
                // Dim background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable { showTimePopup = false } // dismiss if user clicks outside
                )

                // Hover box centered
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(300.dp)
                        .height(300.dp)
                        .background(Color(0xFF003B8E), RoundedCornerShape(24.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Select Time:",
                            fontFamily = Jersey20,
                            fontSize = 32.sp,
                            color = Color.White
                        )

                        Button(
                            onClick = { onEasyClick(30) }, // 30s
                            modifier = Modifier
                                .width(180.dp)
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7)),
                            shape = RoundedCornerShape(24.dp)
                        ) { Text("30 Seconds", fontFamily = JockeyOne, fontSize = 24.sp) }

                        Button(
                            onClick = { onEasyClick(60) }, // 1m
                            modifier = Modifier
                                .width(180.dp)
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7)),
                            shape = RoundedCornerShape(24.dp)
                        ) { Text("1 Minute", fontFamily = JockeyOne, fontSize = 24.sp) }

                        Button(
                            onClick = { onEasyClick(120) }, // 2m
                            modifier = Modifier
                                .width(180.dp)
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7)),
                            shape = RoundedCornerShape(24.dp)
                        ) { Text("2 Minutes", fontFamily = JockeyOne, fontSize = 24.sp) }
                    }
                }
            }

        }
    }
