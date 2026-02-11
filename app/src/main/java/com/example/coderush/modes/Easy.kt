package com.example.coderush.modes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.AnswerButton
import com.example.coderush.GameTimer
import com.example.coderush.Question
import com.example.coderush.R
import com.example.coderush.singleplayer.SingleScore
import com.example.coderush.multiplayer.MultiScore
import com.example.coderush.questions.easyQuestions
import com.example.coderush.ui.theme.Jersey20
import com.example.coderush.ui.theme.JockeyOne
import kotlinx.coroutines.delay

class Easy : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get mode from Intent extras; default to "single"
        val rawMode = intent.getStringExtra("MODE") ?: "single"
        val username = intent.getStringExtra("USERNAME") ?: "PLAYER"
        val mode = if (rawMode.lowercase() == "multi") "multi" else "single"

        // Get time
        val timeInSeconds = intent.getIntExtra("TIME", 30) // default to 30s

        // Shuffle questions
        val shuffledQuestions = easyQuestions.shuffled()
        setContent {
            EasyGameScreen(
                mode = mode,
                totalTime = timeInSeconds,
                questions = shuffledQuestions,
                username = username
            )
        }
    }
}

@Composable
fun EasyGameScreen(
    mode: String,
    totalTime: Int,
    questions: List<Question>,
    username: String
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var moveToNext by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val question = questions[currentQuestionIndex]
    val context = LocalContext.current

    // Auto-next question / end game logic
    LaunchedEffect(moveToNext) {
        if (moveToNext) {
            delay(1000)
            selectedAnswer = null
            showResult = false

            if (currentQuestionIndex + 1 < easyQuestions.size) {
                currentQuestionIndex += 1
            } else {
                // Navigate based on mode
                val targetActivity = if (mode.lowercase() == "multi") MultiScore::class.java else SingleScore::class.java
                context.startActivity(
                    Intent(context, targetActivity).apply {
                        putExtra("SCORE", score)
                        putExtra("USERNAME", username)
                    }
                )
                (context as? ComponentActivity)?.finish()
            }

            moveToNext = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TIMER
            /*Text(
                text = formatTime(currentTime), //00:30
                color = Color(0xFF003B8E),
                fontFamily = Jersey20,
                fontSize = 48.sp,
                modifier = Modifier.padding(top = 32.dp, bottom = 10.dp)
            )*/
            // Countdown timer
            GameTimer(
                totalTime = totalTime,
                onTimeUp = {
                    val targetActivity = if (mode.lowercase() == "multi") MultiScore::class.java else SingleScore::class.java
                    context.startActivity(
                        Intent(context, targetActivity).apply {
                            putExtra("SCORE", score)
                            putExtra("USERNAME", username)
                        }
                    )
                    (context as? ComponentActivity)?.finish()
                }
            )

            // QUESTION BOX
            Box(
                modifier = Modifier
                    .height(260.dp)
                    .fillMaxWidth(0.9f)
                    .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                    .background(Color(0xFF003B8E), RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                // SCORE top-left
                Text(
                    text = "Score: $score",
                    color = Color.White,
                    fontFamily = Jersey20       ,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.TopStart)
                )

                // QUESTION text centered
                Text(
                    text = question.question,
                    color = Color.White,
                    fontFamily = JockeyOne,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ANSWERS (full-width buttons)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                question.choices.forEachIndexed { index, choice ->
                    AnswerButton(
                        text = choice,
                        isSelected = selectedAnswer == index,
                        isCorrect = index == question.correctIndex,
                        showResult = showResult
                    ) {
                        if (selectedAnswer == null) {
                            selectedAnswer = index
                            showResult = true

                            // Increment score if correct
                            if (index == question.correctIndex) {
                                score++
                            }

                            moveToNext = true
                        }
                    }
                }
            }
        }
    }
}


