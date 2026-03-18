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
import com.example.coderush.AppAudio
import com.example.coderush.AnswerButton
import com.example.coderush.GameTimer
import com.example.coderush.Question
import com.example.coderush.R
import com.example.coderush.singleplayer.SingleScore
import com.example.coderush.multiplayer.MultiScore
import com.example.coderush.questions.hardQuestions
import com.example.coderush.ui.theme.Jersey20
import com.example.coderush.ui.theme.JockeyOne
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.delay

class Hard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rawMode       = intent.getStringExtra("MODE") ?: "single"
        val username      = intent.getStringExtra("USERNAME") ?: "PLAYER"
        val mode          = if (rawMode.lowercase() == "multi") "multi" else "single"
        val roomCode      = intent.getStringExtra("ROOM_CODE") ?: ""
        val timeInSeconds = intent.getIntExtra("TIME", 30)
        val shuffledQuestions = hardQuestions.shuffled()
        setContent {
            HardGameScreen(
                mode = mode, totalTime = timeInSeconds,
                questions = shuffledQuestions, username = username, roomCode = roomCode
            )
        }
    }

    override fun onResume() {
        super.onResume()
        AppAudio.playLoop(this, R.raw.game_audio)
    }

    override fun onPause() {
        super.onPause()
        AppAudio.stopLoop()
    }
}

/** Saves score to MultiplayerLeaderboards/{roomCode}/scores/{username} then navigates. */
private fun saveMultiScoreAndNavigate(
    context: android.content.Context,
    roomCode: String,
    username: String,
    score: Int
) {
    val navigate = {
        context.startActivity(
            Intent(context, MultiScore::class.java).apply {
                putExtra("SCORE", score)
                putExtra("USERNAME", username)
                putExtra("ROOM_CODE", roomCode)
            }
        )
        (context as? ComponentActivity)?.finish()
    }
    if (roomCode.isNotEmpty()) {
        FirebaseFirestore.getInstance()
            .collection("MultiplayerLeaderboards").document(roomCode)
            .collection("scores").document(username)
            .set(mapOf("username" to username, "score" to score,
                       "timestamp" to FieldValue.serverTimestamp()))
            .addOnCompleteListener { navigate() }
    } else {
        navigate()
    }
}

@Composable
fun HardGameScreen(
    mode: String,
    totalTime: Int,
    questions: List<Question>,
    username: String,
    roomCode: String = ""
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer       by remember { mutableStateOf<Int?>(null) }
    var showResult           by remember { mutableStateOf(false) }
    var moveToNext           by remember { mutableStateOf(false) }
    var score                by remember { mutableIntStateOf(0) }

    val question = questions[currentQuestionIndex]
    val context  = LocalContext.current

    fun endGame() {
        if (mode == "multi") saveMultiScoreAndNavigate(context, roomCode, username, score)
        else {
            context.startActivity(Intent(context, SingleScore::class.java).apply {
                putExtra("SCORE", score); putExtra("USERNAME", username)
            })
            (context as? ComponentActivity)?.finish()
        }
    }

    LaunchedEffect(moveToNext) {
        if (moveToNext) {
            delay(1000)
            selectedAnswer = null; showResult = false
            if (currentQuestionIndex + 1 < hardQuestions.size) currentQuestionIndex++
            else endGame()
            moveToNext = false
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenH = maxHeight
        val questionBoxH   = (screenH * 0.28f).coerceIn(180.dp, 300.dp)
        val questionFontSp = (screenH.value * 0.028f).coerceIn(15f, 22f)
        val scoreFontSp    = (screenH.value * 0.030f).coerceIn(16f, 26f)
        val btnH           = (screenH * 0.09f).coerceIn(54.dp, 78.dp)
        val btnGap         = (screenH * 0.018f).coerceIn(8.dp, 18.dp)
        val midSpacer      = (screenH * 0.035f).coerceIn(12.dp, 36.dp)

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
            GameTimer(totalTime = totalTime, onTimeUp = { endGame() })

            Box(
                modifier = Modifier
                    .height(questionBoxH)
                    .fillMaxWidth(0.9f)
                    .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                    .background(Color(0xFF003B8E), RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Score: $score",
                    color = Color.White,
                    fontFamily = Jersey20,
                    fontSize = scoreFontSp.sp,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = question.question,
                    color = Color.White,
                    fontFamily = JockeyOne,
                    fontSize = questionFontSp.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(midSpacer))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.spacedBy(btnGap)
            ) {
                question.choices.forEachIndexed { index, choice ->
                    AnswerButton(
                        text = choice,
                        isSelected = selectedAnswer == index,
                        isCorrect  = index == question.correctIndex,
                        showResult = showResult,
                        modifier   = Modifier.height(btnH)
                    ) {
                        if (selectedAnswer == null) {
                            selectedAnswer = index
                            showResult     = true
                            if (index == question.correctIndex) {
                                score++
                                AppAudio.playOneShot(context, R.raw.correct_audio)
                            } else {
                                AppAudio.playOneShot(context, R.raw.incorrect_audio)
                            }
                            moveToNext = true
                        }
                    }
                }
            }
        }
    }
}
