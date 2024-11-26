package kr.boostcamp_2024.course.domain.model

data class QuizResult(
    val choiceQuestions: List<Question>,
    val userOmrAnswers: List<Any>,
) {
    val totalQuestions: Int
        get() = userOmrAnswers.size

    val questionResults: List<QuestionResult>
        get() = userOmrAnswers.mapIndexed { index, userOmrAnswers ->
            QuestionResult(
                choiceQuestion = choiceQuestions[index],
                userAnswer = userOmrAnswers,
                isCorrect = when (userOmrAnswers) {
                    is Number -> evaluateChoiceQuestion(index, userOmrAnswers)
                    is Map<*, *> -> evaluateBlankQuestion(index, userOmrAnswers)
                    else -> false
                },
            )
        }

    val correctQuestions: Int
        get() = questionResults.count { it.isCorrect }

    private fun evaluateChoiceQuestion(
        index: Int,
        userAnswer: Number,
    ): Boolean = userAnswer == (choiceQuestions[index] as ChoiceQuestion).answer

    private fun evaluateBlankQuestion(
        index: Int,
        userAnswer: Map<*, *>,
    ): Boolean {
        val blankQuestion = choiceQuestions[index] as BlankQuestion
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
