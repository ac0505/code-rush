package com.example.coderush.questions

import com.example.coderush.Question

val normalQuestions = listOf(
    Question(
        "What will be the output of the following code?\n\nint x = 5;\ncout << x++;",
        listOf("4", "5", "6", "Compilation error"),
        1
    ),
    Question(
        "Which of the following is used to dynamically allocate memory in C++?",
        listOf("malloc", "alloc", "new", "create"),
        2
    ),
    Question(
        "What does the const keyword do?",
        listOf(
            "Makes a variable global",
            "Prevents a variable from being modified",
            "Speeds up program execution",
            "Allocates memory dynamically"
        ),
        1
    )
)