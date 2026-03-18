package com.example.coderush

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.ui.theme.Jersey20
import com.example.coderush.ui.theme.JockeyOne
import kotlinx.coroutines.delay

@Composable
fun AnswerButton(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showResult: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        showResult && isCorrect -> Color(0xFF4CAF50)        // GREEN
        showResult && isSelected && !isCorrect -> Color(0xFFF44336) // RED
        else -> Color(0xFF3A78D7)                           // BLUE
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .border(2.dp, Color.White, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontFamily = Jersey20,
            fontSize = 24.sp,
            softWrap = true,
            maxLines = 2
        )
    }
}

@Composable
fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
fun GameTimer(
    totalTime: Int,
    onTimeUp: () -> Unit = {}
): Int {
    var currentTime by remember { mutableStateOf(totalTime) }

    // Countdown logic
    LaunchedEffect(currentTime) {
        if (currentTime > 0) {
            delay(1000)
            currentTime--
        } else {
            onTimeUp()
        }
    }

    // Change color if 10s or less
    val textColor = if (currentTime <= 10) Color.Red else Color(0xFF003B8E)

    Text(
        text = formatTime(currentTime),
        color = textColor,
        fontFamily = Jersey20,
        fontSize = 48.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )

    return currentTime
}

@Composable
fun LeaderboardHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "RANK",
            fontSize = 14.sp,
            fontFamily = JockeyOne,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.width(45.dp)
        )
        Text(
            text = "NAME",
            fontSize = 14.sp,
            fontFamily = JockeyOne,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "SCORE",
            fontSize = 14.sp,
            fontFamily = JockeyOne,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.End,
            modifier = Modifier.width(60.dp)
        )
    }
}

@Composable
fun LeaderboardRow(rank: Int, name: String, score: Int, highlight: Boolean = false) {
    val bgColor = when {
        highlight -> Color(0xFF5BA3E0).copy(alpha = 0.40f)
        rank == 1 -> Color(0xFFFFD700).copy(alpha = 0.25f)
        rank == 2 -> Color(0xFFC0C0C0).copy(alpha = 0.25f)
        rank == 3 -> Color(0xFFCD7F32).copy(alpha = 0.25f)
        else      -> Color.White.copy(alpha = 0.05f)
    }

    val borderStroke = if (highlight)   BorderStroke(2.dp, Color(0xFF5BA3E0)) else null

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = borderStroke,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(45.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (rank == 1) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "#$rank",
                        fontSize = 18.sp,
                        fontFamily = JockeyOne,
                        color = Color.White
                    )
                }
            }

            Text(
                text = name,
                fontSize = 18.sp,
                fontFamily = JockeyOne,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "$score",
                fontSize = 20.sp,
                fontFamily = JockeyOne,
                color = Color.White,
                textAlign = TextAlign.End,
                modifier = Modifier.width(60.dp)
            )
        }
    }
}
