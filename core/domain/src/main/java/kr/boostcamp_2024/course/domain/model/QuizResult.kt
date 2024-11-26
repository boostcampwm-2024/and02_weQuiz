package kr.boostcamp_2024.course.domain.model

data class QuizResult(
    val questions: List<Question>,
    val userOmrAnswers: List<Any>,
) {
    val totalQuestions: Int
        get() = userOmrAnswers.size

    val questionResults: List<QuestionResult>
        get() = userOmrAnswers.mapIndexed { index, userOmrAnswers ->
            QuestionResult(
                choiceQuestion = questions[index],
                userAnswer = userOmrAnswers,
                isCorrect = when (userOmrAnswers) {
                    is Int -> evaluateChoiceQuestion(index, userOmrAnswers)
                    is Map<*, *> -> evaluateBlankQuestion(index, userOmrAnswers)
                    else -> throw Exception("Invalid user answer type")
                },
            )
        }

    val correctQuestions: Int
        get() = questionResults.count { it.isCorrect }

    private fun evaluateChoiceQuestion(
        index: Int,
        userAnswer: Int,
    ): Boolean = userAnswer == (questions[index] as ChoiceQuestion).answer

    private fun evaluateBlankQuestion(
        index: Int,
        userAnswer: Map<*, *>,
    ): Boolean {
        val blankQuestion = questions[index] as BlankQuestion
        val blankQuestionContent = blankQuestion.questionContent.filter { it["type"] == "blank" }
        return blankQuestionContent.withIndex().all { (index, content) ->
            content["text"] == userAnswer[index]
        }
    }
}

data class QuestionResult(
    val choiceQuestion: Question,
    val userAnswer: Any,
    val isCorrect: Boolean,
)
