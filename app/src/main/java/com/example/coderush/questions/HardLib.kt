package com.example.coderush.questions

import com.example.coderush.Question

val hardQuestions = listOf(
    Question(
        "What is the output of the following code?\nint x = 5;\nint &r = x;\nr++;\ncout << x;",
        listOf("4", "5", "6", "Compilation error"),
        2
    ),
    Question(
        "What will this code print?\nint x = 10;\ncout << (x > 5 ? x : 5);",
        listOf("5", "10", "true", "false"),
        1
    ),
    Question(
        "Which constructor is called when an object is created with no arguments?",
        listOf("Parameterized constructor", "Copy constructor", "Default constructor", "Destructor"),
        2
    ),
    Question(
        "What happens if a destructor is declared private?",
        listOf("Object cannot be destroyed", "Object cannot be created on stack", "Memory leak occurs", "Compiler error always occurs"),
        1
    ),
    Question(
        "What is the output?\nint a = 5;\ncout << a++ + ++a;",
        listOf("11", "12", "13", "Undefined behavior"),
        3
    ),
    Question(
        "Which keyword prevents inheritance of a class?",
        listOf("static", "final", "sealed", "virtual"),
        1
    ),
    Question(
        "What does virtual keyword ensure?",
        listOf("Faster execution", "Compile-time binding", "Runtime polymorphism", "Memory allocation"),
        2
    ),
    Question(
        "Which STL container allows fast insertion and deletion at both ends?",
        listOf("vector", "list", "deque", "set"),
        2
    ),
    Question(
        "What is the output?\nint arr[] = {1,2,3,4};\ncout << *(arr + 2);",
        listOf("1", "2", "3", "4"),
        2
    ),
    Question(
        "What does the explicit keyword do?",
        listOf("Prevents object creation", "Prevents implicit type conversion", "Enables inheritance", "Increases scope"),
        1
    ),
    Question(
        "What is the output?\nint x = 5;\nint *p = &x;\ncout << *p + 1;",
        listOf("5", "6", "Address value", "Error"),
        1
    ),
    Question(
        "Which operator cannot be overloaded?",
        listOf("+", "[]", "::", "<<"),
        2
    ),
    Question(
        "What does mutable keyword allow?",
        listOf("Variable to change in constant object", "Variable to be inherited", "Variable to be static", "Variable to be global"),
        0
    ),
    Question(
        "What is the output?\nint x = 3;\nint y = x << 1;\ncout << y;",
        listOf("3", "5", "6", "9"),
        2
    ),
    Question(
        "Which type of inheritance causes ambiguity?",
        listOf("Single", "Multiple", "Hierarchical", "Multilevel"),
        1
    ),
    Question(
        "What is a pure virtual function?",
        listOf("Function with no body", "Virtual function returning void", "Function defined in derived class", "Static function"),
        0
    ),
    Question(
        "Which container stores elements in key-value pairs?",
        listOf("vector", "list", "map", "stack"),
        2
    ),
    Question(
        "What is the output?\ncout << sizeof('A');",
        listOf("1", "2", "4", "Depends on compiler"),
        2
    ),
    Question(
        "What does RAII stand for?",
        listOf("Runtime Allocation and Initialization", "Resource Allocation Is Initialization", "Resource Access In Inheritance", "Runtime Access In Initialization"),
        1
    ),
    Question(
        "What happens if a base class destructor is not virtual?",
        listOf("No issue", "Derived destructor may not be called", "Memory increases", "Compilation error"),
        1
    ),
    Question(
        "Which C++ feature resolves the diamond problem?",
        listOf("Abstract classes", "Virtual inheritance", "Function overloading", "Templates"),
        1
    ),
    Question(
        "What is the output?\nint x = 10;\nconst int *p = &x;\n*p = 20;",
        listOf("10", "20", "Compilation error", "Runtime error"),
        2
    ),
    Question(
        "What does nullptr represent?",
        listOf("Integer zero", "Invalid memory", "Type-safe null pointer", "Garbage value"),
        2
    ),
    Question(
        "Which cast is safest in C++?",
        listOf("C-style cast", "reinterpret_cast", "static_cast", "dynamic_cast"),
        3
    ),
    Question(
        "Which keyword allows a function to throw exceptions?",
        listOf("try", "throw", "catch", "exception"),
        1
    ),
    Question(
        "What is the output?\nint a = 2;\ncout << (a == 2 && a++ == 3);",
        listOf("0", "1", "2", "Undefined"),
        0
    ),
    Question(
        "Which header is required for exception handling?",
        listOf("<iostream>", "<exception>", "<stdexcept>", "Both B and C"),
        3
    ),
    Question(
        "What is the output?\nint x = 5;\nint &&r = 10;\ncout << r;",
        listOf("5", "10", "Error", "Undefined"),
        1
    ),
    Question(
        "What does std::move() do?",
        listOf("Copies object", "Deletes object", "Converts to rvalue reference", "Transfers memory automatically"),
        2
    ),
    Question(
        "Which concept improves performance by avoiding unnecessary copies?",
        listOf("Polymorphism", "Inheritance", "Move semantics", "Encapsulation"),
        2
    )
)
