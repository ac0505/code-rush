package com.example.coderush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*


class Easy : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen() {
    // Track which answer was clicked
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.game_bg), // replace with your background drawable
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // makes the image fill the screen nicely
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row with the character image and the question box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(
                        top = 80.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Question box
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            color = Color(0xFF003B8E), // dark blue box background
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Question No.1: What is 2 + 2?",
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnswerButton(
                    label = "A. 3",
                    isSelected = selectedAnswer == 0,
                    isCorrect = false,
                    onClick = { selectedAnswer = 0 }
                )
                AnswerButton(
                    label = "B. 4",
                    isSelected = selectedAnswer == 1,
                    isCorrect = true,
                    onClick = { selectedAnswer = 1 }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnswerButton(
                    label = "C. 5",
                    isSelected = selectedAnswer == 2,
                    isCorrect = false,
                    onClick = { selectedAnswer = 2 }
                )
                AnswerButton(
                    label = "D. 6",
                    isSelected = selectedAnswer == 3,
                    isCorrect = false,
                    onClick = { selectedAnswer = 3 }
                )
            }
        }
    }
}

@Composable
fun AnswerButton(
    label: String,
    isSelected: Boolean = false,
    isCorrect: Boolean = false,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected && isCorrect -> Color(0x8800FF00) // green overlay for correct
        isSelected && !isCorrect -> Color(0x88FF0000) // red overlay for wrong
        else -> Color(0xFF3A78D7) // default
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .height(70.dp)
            .width(140.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor)
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}
