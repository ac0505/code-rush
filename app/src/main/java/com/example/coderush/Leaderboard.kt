package com.example.coderush

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.coderush.ui.theme.Jersey20
import com.example.coderush.ui.theme.JockeyOne
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Leaderboard Activity
 *
 * Extras received via Intent:
 *   SOURCE     : "menu" | "single" | "multi"
 *   SCORE      : Int   (only when SOURCE != "menu")
 *   USERNAME   : String (for multiplayer)
 *   PLAYERS    : ArrayList<String>  "name:score" pairs (multiplayer)
 */
class Leaderboard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val source = intent.getStringExtra("SOURCE") ?: "menu"
        val score = intent.getIntExtra("SCORE", 0)
        val username = intent.getStringExtra("USERNAME") ?: ""
        @Suppress("UNCHECKED_CAST")
        val players = intent.getStringArrayListExtra("PLAYERS") ?: arrayListOf()

        // Save score to Firestore when coming from single-player game
        if (source == "single" && username.isNotEmpty()) {
            saveScore(username, score)
        }

        setContent {
            LeaderboardScreen(
                source = source,
                score = score,
                username = username,
                multiPlayers = players,
                onPlayAgain = {
                    when (source) {
                        "single" -> {
                            startActivity(Intent(this, Difficulty::class.java))
                            finish()
                        }
                        "multi" -> {
                            val intent = Intent(this, Difficulty::class.java)
                            intent.putExtra("MODE", "multi")
                            intent.putExtra("USERNAME", username)
                            startActivity(intent)
                            finish()
                        }
                        else -> finish()
                    }
                },
                onMainMenu = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            )
        }
    }

    private fun saveScore(username: String, score: Int) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("leaderboard").document(username)
        docRef.get().addOnSuccessListener { doc ->
            val existing = doc.getLong("score")?.toInt() ?: 0
            if (score > existing) {
                docRef.set(mapOf("username" to username, "score" to score))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  LEADERBOARD SCREEN
// ─────────────────────────────────────────────────────────────
@Composable
fun LeaderboardScreen(
    source: String,
    score: Int,
    username: String,
    multiPlayers: List<String>,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val isLoggedIn = auth.currentUser != null
    val isFromGame = source == "single" || source == "multi"
    val isMulti = source == "multi"

    // Single-player leaderboard entries fetched from Firestore
    var entries by remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    if (!isMulti && isLoggedIn) {
        LaunchedEffect(Unit) {
            FirebaseFirestore.getInstance()
                .collection("leaderboard")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener { snap ->
                    entries = snap.documents.mapNotNull { doc ->
                        val u = doc.getString("username") ?: return@mapNotNull null
                        val s = doc.getLong("score")?.toInt() ?: 0
                        u to s
                    }
                    loading = false
                }
                .addOnFailureListener { loading = false }
        }
    } else {
        loading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Show "Score: X" only when coming directly after a game
            if (isFromGame && !isMulti) {
                Text(
                    text = "Score:  $score",
                    fontSize = 48.sp,
                    fontFamily = Jersey20,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // ── LEADERBOARD BOX ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF1A3A7A), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = if (isMulti) "MULTIPLAYER RESULTS" else "LEADERBOARD",
                        fontSize = 24.sp,
                        fontFamily = JockeyOne,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    when {
                        !isLoggedIn && !isMulti -> {
                            // Not logged in and from menu → prompt to log in
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Log in to show leaderboard",
                                    fontSize = 18.sp,
                                    fontFamily = JockeyOne,
                                    color = Color.White.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        isMulti -> {
                            // Multiplayer results
                            val parsed = multiPlayers
                                .mapNotNull { entry ->
                                    val parts = entry.split(":")
                                    if (parts.size == 2) parts[0] to (parts[1].toIntOrNull() ?: 0) else null
                                }
                                .sortedByDescending { it.second }

                            if (parsed.isEmpty()) {
                                // Show current player's single entry
                                LeaderboardRow(rank = 1, name = username, score = score, highlight = true)
                            } else {
                                LazyColumn {
                                    itemsIndexed(parsed) { index, (name, s) ->
                                        LeaderboardRow(
                                            rank = index + 1,
                                            name = name,
                                            score = s,
                                            highlight = name == username
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                }
                            }
                        }
                        loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                        entries.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No scores yet.\nBe the first to play!",
                                    fontSize = 16.sp,
                                    fontFamily = JockeyOne,
                                    color = Color.White.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        else -> {
                            LazyColumn {
                                itemsIndexed(entries) { index, (name, s) ->
                                    LeaderboardRow(
                                        rank = index + 1,
                                        name = name,
                                        score = s,
                                        highlight = name == username
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isFromGame) {
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("PLAY AGAIN", fontSize = 20.sp, fontFamily = Jersey20)
                    }
                }

                Button(
                    onClick = onMainMenu,
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .padding(start = if (isFromGame) 8.dp else 0.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("MAIN MENU", fontSize = 20.sp, fontFamily = Jersey20)
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(rank: Int, name: String, score: Int, highlight: Boolean = false) {
    // User's own row always gets the brightest light-blue highlight
    val bgColor = when {
        highlight -> Color(0xFF5BA3E0).copy(alpha = 0.40f) // Light blue — current user
        rank == 1 -> Color(0xFFFFD700).copy(alpha = 0.30f) // Gold
        rank == 2 -> Color(0xFFC0C0C0).copy(alpha = 0.30f) // Silver
        rank == 3 -> Color(0xFFCD7F32).copy(alpha = 0.30f) // Bronze
        else      -> Color.White.copy(alpha = 0.05f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#$rank",
            fontSize = 18.sp,
            fontFamily = JockeyOne,
            color = Color.White,
            modifier = Modifier.width(40.dp)
        )
        Text(
            text = name,
            fontSize = 18.sp,
            fontFamily = JockeyOne,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$score",
            fontSize = 18.sp,
            fontFamily = JockeyOne,
            color = Color.White
        )
    }
}
