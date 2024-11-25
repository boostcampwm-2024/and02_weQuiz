package kr.boostcamp_2024.course.domain.model

data class QuizResult(
    val choiceQuestions: List<ChoiceQuestion>,
    val userOmrAnswers: List<Int>,
) {
    val totalQuestions: Int
        get() = userOmrAnswers.size

    val correctQuestions: Int
        get() = userOmrAnswers.withIndex().count { (index, userAnswer) ->
            userAnswer == choiceQuestions[index].answer
        }

    val questionResults: List<QuestionResult>
        get() = userOmrAnswers.mapIndexed { index, userOmrAnswers ->
            QuestionResult(
                choiceQuestion = choiceQuestions[index],
                userAnswer = userOmrAnswers,
                isCorrect = choiceQuestions[index].answer == userOmrAnswers,
            )
        }
}

data class QuestionResult(
    val choiceQuestion: ChoiceQuestion,
    val userAnswer: Int,
    val isCorrect: Boolean,
)
