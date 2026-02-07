package com.example.coderush.questions

import com.example.coderush.Question

val hardQuestions = listOf(
    Question(
        "Which concept allows the same function name to have different implementations?",
        listOf(
            "Inheritance",
            "Encapsulation",
            "Polymorphism",
            "Abstraction"
        ),
        2
    ),
    Question(
        "What is the output of the following code?\n\nint a = 10;\nint &b = a;\nb = 20;\ncout << a;",
        listOf(
            "10",
            "20",
            "Garbage value",
            "Compilation error"
        ),
        1
    ),
    Question(
        "Which of the following correctly describes a virtual function?",
        listOf(
            "A function that cannot be overridden",
            "A function resolved at compile time",
            "A function resolved at runtime using dynamic binding",
            "A function that must be inline"
        ),
        2
    )
)