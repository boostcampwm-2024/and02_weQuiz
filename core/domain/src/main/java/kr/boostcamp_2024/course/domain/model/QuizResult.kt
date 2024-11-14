package kr.boostcamp_2024.course.domain.model

data class QuizResult(
    val questions: List<Question>,
    val userOmrAnswers: List<Int>,
) {
    val totalQuestions: Int
        get() = userOmrAnswers.size

    val correctQuestions: Int
        get() = userOmrAnswers.withIndex().count { (index, userAnswer) ->
            userAnswer == questions[index].answer
        }

    val questionResults: List<QuestionResult>
        get() = userOmrAnswers.mapIndexed { index, userOmrAnswers ->
            QuestionResult(
                question = questions[index],
                userAnswer = userOmrAnswers,
                isCorrect = questions[index].answer == userOmrAnswers,
            )
        }
}

data class QuestionResult(
    val question: Question,
    val userAnswer: Int,
    val isCorrect: Boolean,
)
