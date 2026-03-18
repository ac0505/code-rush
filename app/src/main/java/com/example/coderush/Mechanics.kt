package com.example.coderush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.ui.theme.Jersey20

class Options : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MechanicsScreen(onBack = { finish() })
        }
    }
}

@Composable
fun MechanicsScreen(onBack: () -> Unit = {}) {

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

        // Back button top-left
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

        // Foreground UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 48.dp, end = 150.dp, top = 150.dp)
        ) {

            // Title
            Text(
                text = "Mechanics",
                fontSize = 60.sp,
                fontFamily = Jersey20,
                color = white
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Volume row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                // Volume pill
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
                        fontSize = 30.sp,
                        fontFamily = Jersey20,
                        color = white
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Volume / Mute icon
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
    }
}
