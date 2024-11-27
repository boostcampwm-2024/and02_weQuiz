package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion

@Composable
fun ChoiceQuestionContent(
    isOwner: Boolean = false,
    isRealTime: Boolean = false,
    question: ChoiceQuestion,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
) {
    Column {
        QuestionTitleAndDetail(
            title = question.title,
            description = question.description,
        )

        if (isRealTime) {
            RealTimeQuestion(
                isOwner = isOwner,
                questions = question.choices,
                selectedIndex = question.answer,
            )
        } else {
            Question(
                questions = question.choices,
                selectedIndex = selectedIndex,
                onOptionSelected = onOptionSelected,
            )
        }
    }
}

@Preview
@Composable
fun ChoiceQuestionContentPreview() {
    WeQuizTheme {
        ChoiceQuestionContent(
            question = ChoiceQuestion(
                id = "",
                title = "다음 중 가장 큰 수를 고르시오.",
                description = "다음 중 가장 큰 수를 고르시오.",
                solution = null,
                choices = listOf(
                    "1",
                    "2",
                    "3",
                    "4",
                ),
                answer = 3,
                userAnswers = listOf(),
                type = "choice",
            ),
            selectedIndex = 0,
            onOptionSelected = {},
        )
    }
}
