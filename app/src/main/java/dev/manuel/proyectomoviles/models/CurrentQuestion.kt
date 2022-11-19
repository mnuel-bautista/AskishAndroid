package dev.manuel.proyectomoviles.models

data class CurrentQuestion(
    val questionId: String = "",
    val question: String = "",
    val correctAnswer: String = "",
    val description: String = "",
    val status: CurrentQuestionStatus = CurrentQuestionStatus.InProgress,
    val answers: Map<String, String> = mapOf()
)


enum class CurrentQuestionStatus {
    InProgress, Completed
}