package com.example.coderush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

class Credits : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreditScreen()
        }
    }
}

@Composable
fun CreditScreen() {
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
                .wrapContentHeight()          // keeps aspect ratio
                .align(Alignment.TopCenter)   // align to top
                .padding(top = 150.dp),        // raise 50.dp from top
            contentScale = ContentScale.FillWidth
        )
    }
}
