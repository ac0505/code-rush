package com.example.coderush.multiplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.coderush.Leaderboard

/**
 * MultiScore forwards to Leaderboard with source="multi".
 * The Leaderboard fetches all players' scores from FireStore
 * MultiplayerLeaderboards/{roomCode}/scores.
 */
class MultiScore : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val score    = intent.getIntExtra("SCORE", 0)
        val username = intent.getStringExtra("USERNAME") ?: "PLAYER"
        val roomCode = intent.getStringExtra("ROOM_CODE") ?: ""

        val intent = Intent(this, Leaderboard::class.java).apply {
            putExtra("SOURCE",    "multi")
            putExtra("SCORE",     score)
            putExtra("USERNAME",  username)
            putExtra("ROOM_CODE", roomCode)
        }
        startActivity(intent)
        finish()
    }
}