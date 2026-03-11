package com.example.coderush

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.delay

/**
 * WaitingRoom – Multiplayer lobby
 * Intent extras:
 *   ROOM_CODE  : String  (6-digit code)
 *   DIFFICULTY : String  (easy/normal/hard)
 *   TIME       : Int     (seconds)
 *   USERNAME   : String
 *   IS_HOST    : Boolean
 */
class WaitingRoom : ComponentActivity() {

    private var roomListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roomCode   = intent.getStringExtra("ROOM_CODE") ?: ""
        val difficulty = intent.getStringExtra("DIFFICULTY") ?: "easy"
        val timeSeconds = intent.getIntExtra("TIME", 60)
        val isHost     = intent.getBooleanExtra("IS_HOST", false)
        val auth       = FirebaseAuth.getInstance()
        val username   = intent.getStringExtra("USERNAME")
            ?: auth.currentUser?.displayName
            ?: auth.currentUser?.email?.substringBefore("@")
            ?: "Player"

        val db = FirebaseFirestore.getInstance()
        val roomRef = db.collection("rooms").document(roomCode)

        // Register this player in the Firestore room document
        if (isHost) {
            roomRef.set(
                mapOf(
                    "host"       to username,
                    "difficulty" to difficulty,
                    "time"       to timeSeconds,
                    "started"    to false,
                    "players"    to listOf(username)
                )
            )
        } else {
            roomRef.update("players", FieldValue.arrayUnion(username))
        }

        setContent {
            WaitingRoomScreen(
                roomCode   = roomCode,
                username   = username,
                isHost     = isHost,
                difficulty = difficulty,
                timeSeconds = timeSeconds,
                onBack = {
                    // Remove player from room on back
                    roomRef.update("players", FieldValue.arrayRemove(username))
                    finish()
                },
                onStartGame = {
                    // Host signals start
                    roomRef.update("started", true)
                },
                onGameStarted = {
                    val targetClass = when (difficulty) {
                        "easy" -> Easy::class.java
                        "hard" -> Hard::class.java
                        else   -> Normal::class.java
                    }
                    val i = Intent(this, targetClass)
                    i.putExtra("MODE", "multi")
                    i.putExtra("TIME", timeSeconds)
                    i.putExtra("USERNAME", username)
                    i.putExtra("ROOM_CODE", roomCode)
                    startActivity(i)
                    finish()
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        roomListener?.remove()
    }
}

// ─────────────────────────────────────────────────────────────
//  WAITING ROOM SCREEN
// ─────────────────────────────────────────────────────────────
@Composable
fun WaitingRoomScreen(
    roomCode: String,
    username: String,
    isHost: Boolean,
    difficulty: String,
    timeSeconds: Int,
    onBack: () -> Unit,
    onStartGame: () -> Unit,
    onGameStarted: () -> Unit
) {
    // Live player list from Firestore
    var players by remember { mutableStateOf(listOf(username)) }
    var gameStarted by remember { mutableStateOf(false) }

    // Firestore listener
    LaunchedEffect(roomCode) {
        val db = FirebaseFirestore.getInstance()
        db.collection("rooms").document(roomCode)
            .addSnapshotListener { snap, _ ->
                if (snap != null && snap.exists()) {
                    @Suppress("UNCHECKED_CAST")
                    val p = snap.get("players") as? List<String> ?: emptyList()
                    players = p
                    val started = snap.getBoolean("started") ?: false
                    if (started && !gameStarted) {
                        gameStarted = true
                    }
                }
            }
    }

    // Navigate when host starts
    LaunchedEffect(gameStarted) {
        if (gameStarted) {
            delay(500)
            onGameStarted()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // ── ROOM CODE DISPLAY ──
            Text(
                text = "GAME ROOM",
                fontFamily = Jersey20,
                fontSize = 28.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A3A7A), RoundedCornerShape(16.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Room Code",
                        fontFamily = JockeyOne,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = roomCode,
                        fontFamily = Jersey20,
                        fontSize = 48.sp,
                        color = Color.White,
                        letterSpacing = 8.sp
                    )
                    Text(
                        text = "${difficulty.replaceFirstChar { it.uppercase() }} • ${if (timeSeconds < 60) "${timeSeconds}s" else "${timeSeconds / 60}min"}",
                        fontFamily = JockeyOne,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Players (${players.size})",
                fontFamily = Jersey20,
                fontSize = 24.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── FLOATING PLAYER AVATARS ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF1A3A7A).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                if (players.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Waiting for players...", fontFamily = JockeyOne, color = Color.White.copy(alpha = 0.6f))
                    }
                } else {
                    // Simple wrapping row of floating avatars
                    val rows = players.chunked(3)
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        rows.forEach { rowPlayers ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                            ) {
                                rowPlayers.forEach { playerName ->
                                    FloatingPlayerAvatar(
                                        name = playerName,
                                        isCurrentUser = playerName == username,
                                        isHost = playerName == players.firstOrNull()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isHost) {
                Button(
                    onClick = onStartGame,
                    enabled = players.size >= 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("START GAME", fontFamily = JockeyOne, fontSize = 26.sp)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color(0xFF1A3A7A).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .border(2.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Waiting for host to start...",
                        fontFamily = JockeyOne,
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  FLOATING PLAYER AVATAR (with vertical bob animation)
// ─────────────────────────────────────────────────────────────
@Composable
fun FloatingPlayerAvatar(
    name: String,
    isCurrentUser: Boolean,
    isHost: Boolean
) {
    // Infinite floating animation – each avatar gets a slight phase offset
    val infiniteTransition = rememberInfiniteTransition(label = "float_$name")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400 + (name.length * 80),  // slight phase difference per player
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y_$name"
    )

    val borderColor = when {
        isCurrentUser -> Color.White
        isHost        -> Color(0xFFFFD700)
        else          -> Color(0xFF5599DD)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = offsetY.dp)
    ) {
        // Avatar circle
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    if (isCurrentUser) Color(0xFF4A47A3) else Color(0xFF1A3A7A),
                    CircleShape
                )
                .border(3.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(1).uppercase(),
                fontFamily = JockeyOne,
                fontSize = 28.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (name.length > 8) name.take(7) + "…" else name,
            fontFamily = JockeyOne,
            fontSize = 13.sp,
            color = if (isCurrentUser) Color.White else Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        if (isHost) {
            Text(
                text = "HOST",
                fontFamily = JockeyOne,
                fontSize = 10.sp,
                color = Color(0xFFFFD700)
            )
        }
    }
}
