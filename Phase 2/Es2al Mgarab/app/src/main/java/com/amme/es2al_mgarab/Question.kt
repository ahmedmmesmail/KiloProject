package com.amme.es2al_mgarab

data class Question(
    val category: String,
    val question: String,
    val answers: List<Answer>
)

data class Answer(
    val text: String,
    val isCorrect: Boolean
)
