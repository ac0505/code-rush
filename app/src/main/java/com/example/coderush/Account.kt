package com.example.coderush

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coderush.ui.theme.JockeyOne
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore


class Account : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            val user = auth.currentUser
            if (user != null) {
                var highScore by remember { mutableStateOf(0) }
                val username = user.displayName ?: user.email?.substringBefore("@") ?: "User"
                // Fetch high score from Firestore
                LaunchedEffect(Unit) {
                    val db = FirebaseFirestore.getInstance()
                    val uid = auth.currentUser?.uid ?: ""
                    if (uid.isNotEmpty()) {
                        db.collection("Leaderboards").document(uid).get()
                            .addOnSuccessListener { doc ->
                                highScore = doc.getLong("score")?.toInt() ?: 0
                            }
                    }
                }

                AccountProfileScreen(
                    displayName = username,
                    highScore = highScore,
                    onLogOut = {
                        auth.signOut()
                        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onBack = { finish() },
                    onUpdateUsername = { newName ->
                        val profileUpdates = userProfileChangeRequest { displayName = newName }
                        user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Username updated!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            } else {
                AuthScreen(
                    onLoginSuccess = { finish() },
                    onBack = { finish() }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  PROFILE SCREEN
// ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountProfileScreen(
    displayName: String,
    highScore: Int,
    onLogOut: () -> Unit,
    onBack: () -> Unit,
    onUpdateUsername: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(displayName) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile picture circle
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .border(2.dp, Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayName.take(1).uppercase(),
                    fontSize = 56.sp,
                    fontFamily = JockeyOne,
                    color = Color(0xFF003B8E)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Username display / edit
            if (isEditing) {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    singleLine = true,
                    label = { Text("Username", fontFamily = JockeyOne, color = Color(0xFF003B8E)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF003B8E),
                        unfocusedTextColor = Color(0xFF003B8E),
                        cursorColor = Color(0xFF003B8E),
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.2f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.2f)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        onUpdateUsername(editedName)
                        isEditing = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A78D7)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("SAVE", fontFamily = JockeyOne, fontSize = 22.sp)
                }
            } else {
                // Display username (no "Username:" prefix) with edit icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = displayName,
                        fontSize = 34.sp,
                        fontFamily = JockeyOne,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        onClick = { isEditing = true },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Username",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // High Score display
            Text(
                text = "Highest Score: $highScore",
                fontSize = 24.sp,
                fontFamily = JockeyOne,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // Log Out / Back
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onLogOut,
                    modifier = Modifier
                        .width(140.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("LOG OUT", fontFamily = JockeyOne, fontSize = 20.sp)
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .width(140.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC3333)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("BACK", fontFamily = JockeyOne, fontSize = 20.sp)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  AUTH SCREEN  (Login / Sign-Up / Forgot Password)
// ─────────────────────────────────────────────────────────────
enum class AuthView { LOGIN, SIGNUP, FORGOT_PASSWORD }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var view by remember { mutableStateOf(AuthView.LOGIN) }

    // Login state
    var loginUsername by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    // Sign-Up state
    var signUpUsername by remember { mutableStateOf("") }
    var signUpEmail    by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var signUpConfirm  by remember { mutableStateOf("") }

    // Forgot password state
    var fpEmail  by remember { mutableStateOf("") }
    var fpSent   by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.code_rush_logo),
                contentDescription = "Game Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                contentScale = ContentScale.Fit
            )

            when (view) {

                // ────── LOGIN ──────
                AuthView.LOGIN -> {
                    TabRow(
                        selectedTabIndex = 0,
                        containerColor = Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White
                    ) {
                        Tab(selected = true, onClick = { view = AuthView.LOGIN },
                            text = { Text("LOG IN", fontFamily = JockeyOne, fontSize = 16.sp) })
                        Tab(selected = false, onClick = { view = AuthView.SIGNUP },
                            text = { Text("SIGN UP", fontFamily = JockeyOne, fontSize = 16.sp) })
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    AuthTextField(value = loginUsername, onValueChange = { loginUsername = it }, label = "Username")
                    Spacer(modifier = Modifier.height(12.dp))
                    AuthTextField(value = loginPassword, onValueChange = { loginPassword = it }, label = "Password", isPassword = true)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Forgot password link
                    TextButton(
                        onClick = { view = AuthView.FORGOT_PASSWORD },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Forgot password?", fontFamily = JockeyOne, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val email = "${loginUsername.trim()}@coderush.app"
                            auth.signInWithEmailAndPassword(email, loginPassword)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Welcome back, $loginUsername!", Toast.LENGTH_SHORT).show()
                                        onLoginSuccess()
                                    } else {
                                        Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("LOGIN", fontFamily = JockeyOne, fontSize = 26.sp)
                    }
                }

                // ────── SIGN UP ──────
                AuthView.SIGNUP -> {
                    TabRow(
                        selectedTabIndex = 1,
                        containerColor = Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White
                    ) {
                        Tab(selected = false, onClick = { view = AuthView.LOGIN },
                            text = { Text("LOG IN", fontFamily = JockeyOne, fontSize = 16.sp) })
                        Tab(selected = true, onClick = { view = AuthView.SIGNUP },
                            text = { Text("SIGN UP", fontFamily = JockeyOne, fontSize = 16.sp) })
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    AuthTextField(value = signUpUsername, onValueChange = { signUpUsername = it }, label = "Username")
                    Spacer(modifier = Modifier.height(10.dp))
                    AuthTextField(value = signUpEmail, onValueChange = { signUpEmail = it }, label = "Email")
                    Spacer(modifier = Modifier.height(10.dp))
                    AuthTextField(value = signUpPassword, onValueChange = { signUpPassword = it }, label = "Password", isPassword = true)
                    Spacer(modifier = Modifier.height(10.dp))
                    AuthTextField(value = signUpConfirm, onValueChange = { signUpConfirm = it }, label = "Confirm Password", isPassword = true)
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            when {
                                signUpUsername.isEmpty() || signUpEmail.isEmpty() || signUpPassword.isEmpty() || signUpConfirm.isEmpty() ->
                                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                signUpPassword != signUpConfirm ->
                                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                signUpPassword.length < 6 ->
                                    Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                else -> {
                                    val email = "${signUpUsername.trim()}@coderush.app"
                                    auth.createUserWithEmailAndPassword(email, signUpPassword)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val profileUpdates = userProfileChangeRequest {
                                                    displayName = signUpUsername.trim()
                                                }
                                                auth.currentUser?.updateProfile(profileUpdates)
                                                Toast.makeText(context, "Account created! Welcome, $signUpUsername!", Toast.LENGTH_SHORT).show()
                                                onLoginSuccess()
                                            } else {
                                                Toast.makeText(context, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("CREATE ACCOUNT", fontFamily = JockeyOne, fontSize = 22.sp)
                    }
                }

                // ────── FORGOT PASSWORD ──────
                AuthView.FORGOT_PASSWORD -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Reset Password",
                        fontFamily = JockeyOne,
                        fontSize = 30.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Enter your email address and we'll send you a password reset link with a verification code.",
                        fontFamily = JockeyOne,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    if (fpSent) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("✓ Reset email sent!", fontFamily = JockeyOne, fontSize = 22.sp, color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Check your inbox for a password reset link from Firebase. Follow the link to set a new password.",
                                    fontFamily = JockeyOne,
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                fpSent = false
                                view = AuthView.LOGIN
                            },
                            modifier = Modifier.fillMaxWidth().height(55.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Text("BACK TO LOGIN", fontFamily = JockeyOne, fontSize = 20.sp)
                        }
                    } else {
                        AuthTextField(value = fpEmail, onValueChange = { fpEmail = it }, label = "Email address")
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                if (fpEmail.isEmpty() || !fpEmail.contains("@")) {
                                    Toast.makeText(context, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                auth.sendPasswordResetEmail(fpEmail.trim())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            fpSent = true
                                        } else {
                                            Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A47A3)),
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Text("SEND RESET EMAIL", fontFamily = JockeyOne, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        TextButton(onClick = { view = AuthView.LOGIN }) {
                            Text("← Back to Login", fontFamily = JockeyOne, fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (view != AuthView.FORGOT_PASSWORD) {
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .width(140.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC3333)),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text("BACK", fontFamily = JockeyOne, fontSize = 20.sp)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  REUSABLE TEXT FIELD
// ─────────────────────────────────────────────────────────────
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color(0xFF003B8E), fontFamily = JockeyOne) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else androidx.compose.ui.text.input.VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF003B8E),
            unfocusedTextColor = Color(0xFF003B8E),
            cursorColor = Color(0xFF003B8E),
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedContainerColor = Color.White.copy(alpha = 0.2f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.2f)
        )
    )
}
