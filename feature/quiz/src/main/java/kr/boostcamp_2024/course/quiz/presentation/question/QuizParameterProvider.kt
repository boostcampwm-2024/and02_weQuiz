package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz

internal class QuizParameterProvider : PreviewParameterProvider<BaseQuiz> {
    override val values: Sequence<BaseQuiz>
        get() = sequenceOf(
            RealTimeQuiz(
                id = "1",
                title = "RealTimeQuiz",
                description = "RealTimeQuiz Description",
                questions = listOf("1", "2"),
                userOmrs = listOf("1", "2"),
                currentQuestion = 1,
                ownerId = "1",
                isStarted = true,
                isFinished = false,
                waitingUsers = listOf("1", "2"),
                quizImageUrl = null,
            ),
            Quiz(
                id = "2",
                title = "Quiz",
                description = "Quiz Description",
                startTime = "2022-01-01",
                solveTime = 10,
                questions = listOf("1", "2"),
                userOmrs = listOf("1", "2"),
                quizImageUrl = null,
            ),
        )
}
