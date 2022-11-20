package dev.manuel.proyectomoviles.repositories

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.models.CurrentQuestion
import dev.manuel.proyectomoviles.models.CurrentQuestionStatus
import dev.manuel.proyectomoviles.models.Sala
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QuizzRoomRepository {

    private val firestore = AppDatabase.getDatabase()?.firestore

    private val mQuizRooms: MutableStateFlow<List<Sala>> =
        MutableStateFlow(emptyList())

    val quizRooms: StateFlow<List<Sala>> = mQuizRooms.asStateFlow()

    private val mQuizRoomStatus: MutableStateFlow<QuizRoomStatus> =
        MutableStateFlow(QuizRoomStatus.NotStarted)

    val quizRoomStatus: Flow<QuizRoomStatus> = mQuizRoomStatus

    private val mQuizGroup: MutableStateFlow<String> =
        MutableStateFlow("")

    val quizGroup: StateFlow<String> = mQuizGroup

    private val mCurrentQuestion: MutableStateFlow<CurrentQuestion> = MutableStateFlow(CurrentQuestion())

    val currentQuestion: StateFlow<CurrentQuestion> = mCurrentQuestion.asStateFlow()

    private val mParticipantsCount: MutableStateFlow<Int> = MutableStateFlow(0)

    val participantsCount: Flow<Int> = mParticipantsCount

    @Suppress("UNCHECKED_CAST")
    fun getQuizRoom(quizRoomId: String) {
        firestore?.document("salas/$quizRoomId")?.addSnapshotListener { value, _ ->
                if (value != null) {
                    val status = getQuizRoomStatus(value.getString("estado_sala"))
                    mQuizRoomStatus.value = status

                    val currentQuestion = getCurrentQuestion(value)
                    mCurrentQuestion.value = currentQuestion

                    val participants = value.get("participantes") as HashMap<String, Boolean>
                    mParticipantsCount.value = participants.count()

                    val group = value.getString("cuestionario.nombre") ?: ""
                    mQuizGroup.value = group
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    fun getAllQuizRooms(userId: String) {
        firestore?.collection("salas")
            ?.whereEqualTo("invitados.jaYl9hlDSAHCTWzA2ez5YWc1VhrQ", true)
            ?.whereNotEqualTo("estado_sala", "Completed")
            ?.addSnapshotListener { value, _ ->
                val rooms = value?.documents?.map { e ->
                    val id = e.id
                    val cuestionario = e["cuestionario.nombre"] as String
                    val grupo = e["grupo.nombre"] as String
                    val participantes = (e["participantes"] as HashMap<String, Boolean>).count()

                    Sala(id, cuestionario, grupo, participantes)
                } ?: emptyList()

                mQuizRooms.value = rooms
            }
    }

    suspend fun addAnswer(questionId: String, questionName: String, userId: String, quizRoomId: String, answer: String): Boolean {
        return suspendCoroutine { cont ->
            firestore?.collection("salas")
                ?.document(quizRoomId)
                ?.collection("resultados")
                ?.document(questionId)
                ?.set(
                    mapOf(
                        answer to mapOf(userId to true),
                        "question" to questionName
                    ), SetOptions.merge()
                )
                ?.addOnSuccessListener {
                    cont.resume(true)
                }?.addOnFailureListener { cont.resume(false) }
        }
    }

}

@Suppress("UNCHECKED_CAST")
private fun getCurrentQuestion(quizRoom: DocumentSnapshot): CurrentQuestion {
    val questionId = quizRoom.getString("question.questionId") ?: ""
    val question = quizRoom.getString("question.question") ?: ""
    val correctAnswer = quizRoom.getString("question.correctAnswer") ?: ""
    val description = quizRoom.getString("question.description") ?: ""
    val status = getQuestionStatus(quizRoom.getString("question.status"))
    val answers = quizRoom.get("question.answers") as HashMap<String, String>? ?: mapOf()

    return CurrentQuestion(questionId, question, correctAnswer, description, status, answers)
}

private fun getQuestionStatus(questionStatus: String?): CurrentQuestionStatus {
    return when (questionStatus) {
        "In Progress" -> CurrentQuestionStatus.InProgress
        else -> CurrentQuestionStatus.Completed
    }
}

private fun getQuizRoomStatus(quizRoomStatus: String?): QuizRoomStatus {
    return when (quizRoomStatus) {
        "En Progreso" -> QuizRoomStatus.InProgress
        "Completada" -> QuizRoomStatus.Completed
        else -> QuizRoomStatus.NotStarted
    }
}

enum class QuizRoomStatus {
    NotStarted, InProgress, Completed
}