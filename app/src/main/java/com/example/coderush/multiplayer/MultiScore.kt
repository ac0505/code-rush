package com.example.coderush.multiplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.coderush.Leaderboard

/**
 * MultiScore now forwards to Leaderboard with source="multi".
 * The Leaderboard shows multiplayer results ranked by score.
 */
class MultiScore : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val score = intent.getIntExtra("SCORE", 0)
        val username = intent.getStringExtra("USERNAME") ?: "PLAYER"
        @Suppress("UNCHECKED_CAST")
        val players = intent.getStringArrayListExtra("PLAYERS") ?: arrayListOf(
            // Fallback: build a one-entry players list from this player's data
            "$username:$score"
        )

        val intent = Intent(this, Leaderboard::class.java).apply {
            putExtra("SOURCE", "multi")
            putExtra("SCORE", score)
            putExtra("USERNAME", username)
            putStringArrayListExtra("PLAYERS", players)
        }
        startActivity(intent)
        finish()
    }
}