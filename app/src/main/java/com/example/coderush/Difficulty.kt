package com.example.coderush

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.modes.Easy
import com.example.coderush.modes.Hard
import com.example.coderush.modes.Normal
import com.example.coderush.ui.theme.Jersey20
import com.example.coderush.ui.theme.JockeyOne
import com.google.firebase.auth.FirebaseAuth
import com.example.coderush.AppAudio
import com.example.coderush.R

class Difficulty : ComponentActivity() {

    private lateinit var mode: String
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mode = intent.getStringExtra("MODE") ?: "single"
        val auth = FirebaseAuth.getInstance()
        username = intent.getStringExtra("USERNAME")
            ?: auth.currentUser?.displayName
            ?: auth.currentUser?.email?.substringBefore("@")
            ?: "Player"

        setContent {
            DifficultyScreen(
                mode = mode,
                username = username,
                onBack = { finish() },
                onStartGame = { difficulty, seconds ->
                    val targetClass = when (difficulty) {
                        "easy"   -> Easy::class.java
                        "hard"   -> Hard::class.java
                        else     -> Normal::class.java
                    }
                    val intent = Intent(this, targetClass)
                    intent.putExtra("MODE", mode)
                    intent.putExtra("TIME", seconds)
                    intent.putExtra("USERNAME", username)
                    startActivity(intent)
                    finish()
                },
                onGoToWaitingRoom = { roomCode, difficulty, seconds ->
                    val intent = Intent(this, WaitingRoom::class.java)
                    intent.putExtra("ROOM_CODE", roomCode)
                    intent.putExtra("DIFFICULTY", difficulty)
                    intent.putExtra("TIME", seconds)
                    intent.putExtra("USERNAME", username)
                    intent.putExtra("IS_HOST", true)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        AppAudio.playLoop(this, R.raw.mainscreen_audio)
    }

    override fun onPause() {
        super.onPause()
        AppAudio.stopLoop()
    }
}

// ─────────────────────────────────────────────────────────────
//  DIFFICULTY SCREEN
// ─────────────────────────────────────────────────────────────
@Composable
fun DifficultyScreen(
    mode: String,
    username: String,
    onBack: () -> Unit,
    onStartGame: (difficulty: String, seconds: Int) -> Unit,
    onGoToWaitingRoom: (roomCode: String, difficulty: String, seconds: Int) -> Unit
) {
    val isMulti = mode.lowercase() == "multi"

    // which difficulty button was clicked
    var pendingDifficulty by remember { mutableStateOf("") }
    var showTimePopup    by remember { mutableStateOf(false) }
    // time selection state (single: show Play! confirm; multi: show Code + Next)
    var selectedSeconds  by remember { mutableStateOf(0) }
    var showConfirmPanel by remember { mutableStateOf(false) }  // single: "Play!" step
    var showRoomPanel    by remember { mutableStateOf(false) }  // multi: room code entry step

    // For multi: room code (host generates; others enter)
    var roomCodeInput    by remember { mutableStateOf("") }
    val generatedCode    by remember { mutableStateOf((100000..999999).random().toString()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

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

            // EASY
            DifficultyButton("Easy Mode") {
                pendingDifficulty = "easy"
                showTimePopup = true
            }
            Spacer(modifier = Modifier.height(32.dp))

            // NORMAL
            DifficultyButton("Normal Mode") {
                pendingDifficulty = "normal"
                showTimePopup = true
            }
            Spacer(modifier = Modifier.height(32.dp))

            // HARD
            DifficultyButton("Hard Mode") {
                pendingDifficulty = "hard"
                showTimePopup = true
            }
        }

        // ── TIME PICKER POPUP ──
        if (showTimePopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { showTimePopup = false }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(300.dp)
                    .background(Color(0xFF003B8E), RoundedCornerShape(24.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Select Time:", fontFamily = Jersey20, fontSize = 32.sp, color = Color.White)

                    listOf(30 to "30 Seconds", 60 to "1 Minute", 120 to "2 Minutes").forEach { (secs, label) ->
                        val isSelected = selectedSeconds == secs
                        Button(
                            onClick = { selectedSeconds = secs },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFF2255AA) else Color(0xFF3A78D7)
                            ),
                            border = if (isSelected) BorderStroke(2.dp, Color.White) else null,
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(label, fontFamily = JockeyOne, fontSize = 22.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // "Play!" for single, "Next →" for multi — only enabled when a time is chosen
                    Button(
                        onClick = {
                            if (selectedSeconds > 0) {
                                showTimePopup = false
                                if (isMulti) showRoomPanel = true
                                else         showConfirmPanel = true
                            }
                        },
                        enabled = selectedSeconds > 0,
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text(
                            if (isMulti) "Next →" else "Play!",
                            fontFamily = JockeyOne,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }

        // ── SINGLE: PLAY CONFIRM PANEL ──
        if (showConfirmPanel && !isMulti) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { showConfirmPanel = false }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(300.dp)
                    .background(Color(0xFF003B8E), RoundedCornerShape(24.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${pendingDifficulty.replaceFirstChar { it.uppercase() }} Mode",
                        fontFamily = Jersey20,
                        fontSize = 30.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${if (selectedSeconds < 60) "$selectedSeconds Seconds" else "${selectedSeconds / 60} Minute${if (selectedSeconds / 60 > 1) "s" else ""}"}",
                        fontFamily = JockeyOne,
                        fontSize = 22.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showConfirmPanel = false
                            onStartGame(pendingDifficulty, selectedSeconds)
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("PLAY!", fontFamily = JockeyOne, fontSize = 26.sp)
                    }
                    OutlinedButton(
                        onClick = { showConfirmPanel = false },
                        modifier = Modifier.fillMaxWidth().height(45.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("CANCEL", fontFamily = JockeyOne, fontSize = 20.sp, color = Color.White)
                    }
                }
            }
        }

        // ── MULTI: ROOM CODE PANEL ──
        if (showRoomPanel && isMulti) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { showRoomPanel = false }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(320.dp)
                    .background(Color(0xFF003B8E), RoundedCornerShape(24.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Your Room Code",
                        fontFamily = Jersey20,
                        fontSize = 26.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    // Display generated room code prominently
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1A3A7A), RoundedCornerShape(12.dp))
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = generatedCode,
                            fontFamily = Jersey20,
                            fontSize = 42.sp,
                            color = Color.White,
                            letterSpacing = 6.sp
                        )
                    }
                    Text(
                        "Share this code with other players",
                        fontFamily = JockeyOne,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Divider(color = Color.White.copy(alpha = 0.3f))

                    Text(
                        "Or join with a code:",
                        fontFamily = JockeyOne,
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    OutlinedTextField(
                        value = roomCodeInput,
                        onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) roomCodeInput = it },
                        placeholder = { Text("6-digit code", color = Color(0xFF003B8E), fontFamily = JockeyOne) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().height(58.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF003B8E),
                            unfocusedTextColor = Color(0xFF003B8E),
                            cursorColor = Color(0xFF003B8E),
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedContainerColor = Color.White.copy(alpha = 0.9f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // HOST: go to waiting room as host
                    Button(
                        onClick = {
                            showRoomPanel = false
                            onGoToWaitingRoom(generatedCode, pendingDifficulty, selectedSeconds)
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("HOST GAME", fontFamily = JockeyOne, fontSize = 22.sp)
                    }

                    // JOIN: go to waiting room with entered code
                    Button(
                        onClick = {
                            if (roomCodeInput.length == 6) {
                                showRoomPanel = false
                                onGoToWaitingRoom(roomCodeInput, pendingDifficulty, selectedSeconds)
                            }
                        },
                        enabled = roomCodeInput.length == 6,
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7)),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("JOIN GAME", fontFamily = JockeyOne, fontSize = 22.sp)
                    }

                    OutlinedButton(
                        onClick = { showRoomPanel = false },
                        modifier = Modifier.fillMaxWidth().height(45.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("CANCEL", fontFamily = JockeyOne, fontSize = 20.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DifficultyButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .border(2.dp, Color.White, RoundedCornerShape(24.dp))
            .height(70.dp)
            .width(220.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7))
    ) {
        Text(label, fontSize = 28.sp, fontFamily = JockeyOne)
    }
}
