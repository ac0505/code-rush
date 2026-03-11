package com.example.coderush.singleplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.coderush.Leaderboard
import com.google.firebase.auth.FirebaseAuth

/**
 * SingleScore now simply forwards to Leaderboard with source="single".
 * The Leaderboard screen shows the Score header and saves the high score to Firestore.
 */
class SingleScore : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val score = intent.getIntExtra("SCORE", 0)
        val auth = FirebaseAuth.getInstance()
        val username = auth.currentUser?.displayName
            ?: auth.currentUser?.email?.substringBefore("@")
            ?: ""

        val intent = Intent(this, Leaderboard::class.java).apply {
            putExtra("SOURCE", "single")
            putExtra("SCORE", score)
            putExtra("USERNAME", username)
        }
        startActivity(intent)
        finish()
    }
}
