package com.example.coderush

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.coderush.ui.theme.Jersey20
import com.example.coderush.ui.theme.JockeyOne
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppAudio.init(this)
        setContent {
            MainScreen(
                context              = this,
                onSinglePlayerClick  = { openDifficultyScreen("single") },
                onMultiPlayerClick   = { openMultiPlayerFlow() },
                onLeaderboardClick   = { openLeaderboard() },
                onHowToPlayClick     = { openAboutScreen() },
                onCreditsClick       = { openCreditScreen() },
                onAccountClick       = { openAccountScreen() }
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

    private fun openDifficultyScreen(mode: String) {
        startActivity(Intent(this, Difficulty::class.java).apply { putExtra("MODE", mode) })
    }

    private fun openMultiPlayerFlow() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivity(Intent(this, Difficulty::class.java).apply {
                putExtra("MODE", "multi")
                putExtra("USERNAME", auth.currentUser?.displayName
                    ?: auth.currentUser?.email?.substringBefore("@") ?: "Player")
            })
        } else {
            startActivity(Intent(this, Account::class.java))
        }
    }

    private fun openLeaderboard() {
        startActivity(Intent(this, Leaderboard::class.java).apply { putExtra("SOURCE", "menu") })
    }

    private fun openAboutScreen()   = startActivity(Intent(this, About::class.java))
    private fun openCreditScreen()  = startActivity(Intent(this, Credits::class.java))
    private fun openAccountScreen() = startActivity(Intent(this, Account::class.java))
}

// ─────────────────────────────────────────────────────────────
//  SETTINGS DIALOG
// ─────────────────────────────────────────────────────────────
@Composable
fun SettingsDialog(context: android.content.Context, onDismiss: () -> Unit) {
    var isMuted     by remember { mutableStateOf(AppAudio.isMuted) }
    var musicVolume by remember { mutableStateOf(AppAudio.musicVolume) }
    var sfxVolume   by remember { mutableStateOf(AppAudio.sfxVolume) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF003B8E),
            border = BorderStroke(2.dp, Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Settings", fontFamily = Jersey20, fontSize = 30.sp, color = Color.White)

                Divider(color = Color.White.copy(alpha = 0.3f))

                // Mute toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isMuted) "🔇  Muted" else "🔊  Sound On",
                        fontFamily = JockeyOne, fontSize = 18.sp, color = Color.White
                    )
                    Switch(
                        checked = !isMuted,
                        onCheckedChange = { on ->
                            isMuted = !on
                            AppAudio.isMuted = isMuted
                            AppAudio.applyMusicVolume()
                            AppAudio.saveSettings(context)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor   = Color(0xFF4CAF50),
                            checkedTrackColor   = Color(0xFF1B5E20),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.DarkGray
                        )
                    )
                }

                Divider(color = Color.White.copy(alpha = 0.2f))

                // Music volume
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🎵  Music Volume", fontFamily = JockeyOne, fontSize = 16.sp, color = Color.White)
                        Text("${(musicVolume * 100).toInt()}%", fontFamily = JockeyOne, fontSize = 16.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                    Slider(
                        value = musicVolume,
                        onValueChange = { v ->
                            musicVolume = v
                            AppAudio.musicVolume = v
                            AppAudio.applyMusicVolume()
                        },
                        onValueChangeFinished = { AppAudio.saveSettings(context) },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor        = Color(0xFFFFD700),
                            activeTrackColor  = Color(0xFF3A78D7),
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )
                }

                // SFX volume
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🔔  SFX Volume", fontFamily = JockeyOne, fontSize = 16.sp, color = Color.White)
                        Text("${(sfxVolume * 100).toInt()}%", fontFamily = JockeyOne, fontSize = 16.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                    Slider(
                        value = sfxVolume,
                        onValueChange = { v ->
                            sfxVolume = v
                            AppAudio.sfxVolume = v
                        },
                        onValueChangeFinished = { AppAudio.saveSettings(context) },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor        = Color(0xFFFFD700),
                            activeTrackColor  = Color(0xFF3A78D7),
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )
                }

                Divider(color = Color.White.copy(alpha = 0.2f))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("CLOSE", fontFamily = JockeyOne, fontSize = 20.sp)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  MAIN SCREEN  (Responsive with BoxWithConstraints)
// ─────────────────────────────────────────────────────────────
@Composable
fun MainScreen(
    context: android.content.Context,
    onSinglePlayerClick: () -> Unit,
    onMultiPlayerClick:  () -> Unit,
    onLeaderboardClick:  () -> Unit,
    onHowToPlayClick:    () -> Unit,
    onCreditsClick:      () -> Unit,
    onAccountClick:      () -> Unit
) {
    var showSettings by remember { mutableStateOf(false) }

    if (showSettings) {
        SettingsDialog(context = context, onDismiss = { showSettings = false })
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenH        = maxHeight
        val logoBottomPad  : Dp = (screenH * 0.06f).coerceIn(24.dp, 56.dp)
        val mainBtnH       : Dp = (screenH * 0.09f).coerceIn(52.dp, 80.dp)
        val mainBtnW       : Dp = maxWidth.coerceAtMost(260.dp)
        val mainBtnGap     : Dp = (screenH * 0.02f).coerceIn(8.dp, 20.dp)
        val smallRowPad    : Dp = (maxWidth * 0.14f).coerceIn(40.dp, 80.dp)
        val mainFontSize   = (screenH.value * 0.038f).coerceIn(20f, 32f)
        val smallFontSize  = (screenH.value * 0.022f).coerceIn(13f, 20f)

        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Settings icon top-left
        IconButton(
            onClick = { showSettings = true },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start=16.dp, end=16.dp, top=32.dp, bottom=16.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // Account icon top-right
        IconButton(
            onClick = onAccountClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(start=16.dp, end=16.dp, top=32.dp, bottom=16.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Account",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.game_name),
                contentDescription = "Game Name",
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(bottom = logoBottomPad)
            )

            listOf(
                "Single Player" to onSinglePlayerClick,
                "Multi Player"  to onMultiPlayerClick,
                "LeaderBoard"   to onLeaderboardClick
            ).forEach { (label, action) ->
                Button(
                    onClick = action,
                    modifier = Modifier
                        .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                        .height(mainBtnH)
                        .width(mainBtnW),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7))
                ) {
                    Text(label, fontFamily = JockeyOne, fontSize = mainFontSize.sp)
                }
                Spacer(modifier = Modifier.height(mainBtnGap))
            }

            Spacer(modifier = Modifier.height(mainBtnGap))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = smallRowPad),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onHowToPlayClick,
                    modifier = Modifier.width(120.dp).height(40.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("About", fontSize = smallFontSize.sp, fontFamily = JockeyOne)
                }
                Button(
                    onClick = onCreditsClick,
                    modifier = Modifier.width(120.dp).height(40.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("Credits", fontSize = smallFontSize.sp, fontFamily = JockeyOne)
                }
            }
        }
    }
}
