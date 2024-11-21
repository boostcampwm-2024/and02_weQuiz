package kr.boostcamp_2024.course.domain.model

sealed class BaseQuiz {
    abstract val id: String
    abstract val title: String
    abstract val description: String?
    abstract val questions: List<String>
    abstract val userOmrs: List<String>
    abstract val quizImageUrl: String?
}

data class RealTimeQuiz(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val questions: List<String>,
    override val userOmrs: List<String>,
    val currentQuestion: Int,
    val ownerId: String,
    val isStarted: Boolean,
    val isFinished: Boolean,
    val waitingUsers: Int,
    override val quizImageUrl: String?,
) : BaseQuiz()
