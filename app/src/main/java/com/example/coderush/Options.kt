package com.example.coderush

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.ui.theme.JockeyOne

class Options : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptionScreen(
                onBackClick = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            )
        }
    }
}

@Composable
fun OptionScreen(
    onBackClick: () -> Unit
) {

    var isMuted by remember { mutableStateOf(false) }

    val pillBlue = Color(0xFF3F6EDC)
    val white = Color.White

    Box(modifier = Modifier.fillMaxSize()) {

        // Background image
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        //Foreground UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp, start = 48.dp, end = 48.dp) // Adjusted padding
        ) {

            //Title
            Text(
                text = "Options",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = white
            )

            Spacer(modifier = Modifier.height(32.dp))

            //Volume row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                //Volume pill
                Box(
                    modifier = Modifier
                        .background(
                            color = pillBlue,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Volume",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = white
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                //Volume & Mute icon
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isMuted)
                                R.drawable.mute
                            else
                                R.drawable.volume
                        ),
                        contentDescription = if (isMuted) "Muted" else "Volume On",
                        tint = Color.Unspecified
                    )
                }
            }
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                .height(60.dp)
                .width(150.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Back", fontSize = 30.sp, fontFamily = JockeyOne)
        }
    }
}
