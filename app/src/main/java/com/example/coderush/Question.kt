package com.example.coderush

data class Question(
    val question: String,
    val choices: List<String>,
    val correctIndex: Int
)