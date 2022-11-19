package dev.manuel.proyectomoviles.models

data class Question(
    val questionId: String,
    val question: String,
    val correctAnswer: String,
    val description: String,
    val answers: Map<String, String>
)