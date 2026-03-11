package com.example.coderush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

class Credits : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreditScreen(onBack = { finish() })
        }
    }
}

@Composable
fun CreditScreen(onBack: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Background image
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Developer image full width, 50.dp from top
        Image(
            painter = painterResource(id = R.drawable.developers),
            contentDescription = "Developer",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.TopCenter)
                .padding(top = 150.dp),
            contentScale = ContentScale.FillWidth
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
    }
}
