package com.example.coderush.multiplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.coderush.Account

/**
 * MultiCreate is kept for backward compatibility.
 * It immediately redirects to the unified Account screen (Sign Up tab).
 */
class MultiCreate : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, Account::class.java))
        finish()
    }
}
