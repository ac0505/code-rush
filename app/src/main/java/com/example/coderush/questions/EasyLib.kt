package com.example.coderush.questions

import com.example.coderush.Question

val easyQuestions = listOf(
    Question(
        "Which of the following is the correct way to include the iostream library in C++?",
        listOf(
            "#include <iostream>",
            "#include \"iostream\"",
            "using namespace iostream;",
            "import iostream;"
        ),
        0
    ),
    Question(
        "Which symbol is used to end a statement in C++?",
        listOf(":", ".", ";", ","),
        2
    ),
    Question(
        "What is the correct data type to store a single character in C++?",
        listOf("string", "char", "int", "bool"),
        1
    )
)