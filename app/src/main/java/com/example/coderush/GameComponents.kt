package com.example.coderush

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.coderush.ui.theme.Jersey20
import com.example.coderush.ui.theme.JockeyOne
import kotlinx.coroutines.delay

/**
 * AnswerButton – shows the answer choice with correct/incorrect feedback colours.
 * Accepts an optional [modifier] so callers can override the default height.
 */
@Composable
fun AnswerButton(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showResult: Boolean = false,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(62.dp),
    onClick: () -> Unit
) {
    val backgroundColor = when {
        showResult && isCorrect               -> Color(0xFF4CAF50) // GREEN
        showResult && isSelected && !isCorrect -> Color(0xFFF44336) // RED
        else                                  -> Color(0xFF3A78D7) // BLUE
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .border(2.dp, Color.White, RoundedCornerShape(50.dp)),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontFamily = JockeyOne,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            softWrap = true,
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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

    LaunchedEffect(currentTime) {
        if (currentTime > 0) {
            delay(1000)
            currentTime--
        } else {
            onTimeUp()
        }
    }

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
