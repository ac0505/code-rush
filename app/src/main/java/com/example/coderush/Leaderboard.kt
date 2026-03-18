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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
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
import com.google.firebase.firestore.FieldValue
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
        val roomCode = intent.getStringExtra("ROOM_CODE") ?: ""

        val auth = FirebaseAuth.getInstance()
        val currentUid = auth.currentUser?.uid ?: ""

        setContent {
            // State for triggering refresh after Firestore save
            var scoreSavedTrigger by remember { mutableIntStateOf(0) }

            // Save score to Firestore when coming from a game
            LaunchedEffect(Unit) {
                if ((source == "single" || source == "multi") && username.isNotEmpty() && username != "PLAYER") {
                    saveScore(currentUid, username, score, source, roomCode) {
                        scoreSavedTrigger++ // Trigger refresh after save
                    }
                }
            }

            LeaderboardScreen(
                source = source,
                score = score,
                username = username,
                multiPlayers = players,
                roomCode = roomCode,
                refreshTrigger = scoreSavedTrigger,
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

    private fun saveScore(uid: String, username: String, score: Int, source: String, roomCode: String, onComplete: () -> Unit) {
        if (uid.isEmpty() && source == "single") {
            onComplete()
            return
        }
        val db = FirebaseFirestore.getInstance()

        // 1. Save to global leaderboards (for single and multi)
        val globalDocRef = db.collection("Leaderboards").document(uid.ifEmpty { username })
        globalDocRef.get().addOnSuccessListener { doc ->
            val existing = doc.getLong("score")?.toInt() ?: 0
            if (score > existing) {
                globalDocRef.set(mapOf("username" to username, "score" to score))
            }
        }

        // 2. Save to temporary multiplayer leaderboard if applicable
        if (source == "multi" && roomCode.isNotEmpty()) {
            val multiData = mapOf(
                "username" to username,
                "score" to score,
                "roomCode" to roomCode,
                "timestamp" to FieldValue.serverTimestamp()
            )
            // Use a unique ID for this entry: roomCode + uid (or username if uid is empty)
            val entryId = "${roomCode}_${uid.ifEmpty { username }}"
            db.collection("MultiplayerLeaderboards").document(entryId)
                .set(multiData)
                .addOnCompleteListener { onComplete() }
        } else {
            onComplete()
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
    roomCode: String = "",
    refreshTrigger: Int = 0,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val isLoggedIn = auth.currentUser != null
    val isFromGame = source == "single" || source == "multi"
    val isMulti = source == "multi"

    val currentUid = auth.currentUser?.uid ?: ""
    var fetchedUsername by remember { mutableStateOf("") }
    var entries by remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Fetch the stored username and the leaderboard entries
    if (isLoggedIn) {
        LaunchedEffect(refreshTrigger) {
            val db = FirebaseFirestore.getInstance()
            
            // 1. Fetch user's actual saved username from Firestore
            db.collection("Leaderboards").document(currentUid).get()
                .addOnSuccessListener { doc ->
                    fetchedUsername = doc.getString("username") ?: ""
                }

            // 2. Fetch standings
            loading = true
            if (isMulti && roomCode.isNotEmpty()) {
                // Fetch from temporary multiplayer collection for this room
                db.collection("MultiplayerLeaderboards")
                    .whereEqualTo("roomCode", roomCode)
                    .orderBy("score", Query.Direction.DESCENDING)
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
            } else {
                // Fetch global standings
                db.collection("Leaderboards")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(50)
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
        }
    } else {
        loading = false
    }

    val finalResolvedUsername = remember(username, fetchedUsername) {
        when {
            username.isNotEmpty() && username != "PLAYER" -> username
            fetchedUsername.isNotEmpty() -> fetchedUsername
            else -> auth.currentUser?.displayName ?: auth.currentUser?.email?.substringBefore("@") ?: "PLAYER"
        }
    }

    // Merge fetched entries with current session score
    val finalEntries = remember(entries, score, finalResolvedUsername, isFromGame) {
        val list = entries.toMutableList()
        // If coming from a game session, ensure the current score is reflected in the global table
        if (isFromGame && finalResolvedUsername.isNotEmpty()) {
            val existingIdx = list.indexOfFirst { it.first == finalResolvedUsername }
            if (existingIdx != -1) {
                if (score > list[existingIdx].second) {
                    list[existingIdx] = finalResolvedUsername to score
                }
            } else {
                list.add(finalResolvedUsername to score)
            }
        }
        list.sortedByDescending { it.second }.distinctBy { it.first }
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

                    if (isLoggedIn || isFromGame) {
                        LeaderboardHeader()
                        Divider(
                            color = Color.White.copy(alpha = 0.2f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    when {
                        !isLoggedIn && !isFromGame -> {
                            // Not logged in and not from game → prompt to log in
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
                        loading && entries.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                        finalEntries.isEmpty() -> {
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
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 8.dp)
                            ) {
                                itemsIndexed(finalEntries) { index, (name, s) ->
                                    LeaderboardRow(
                                        rank = index + 1,
                                        name = name,
                                        score = s,
                                        highlight = name == finalResolvedUsername
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
