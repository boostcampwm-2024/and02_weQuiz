package kr.boostcamp_2024.course.quiz.component

import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.R

@Composable
internal fun QuizContent(
    isOwner: Boolean = false,
    isRealTime: Boolean = false,
    currentPage: Int,
    selectedIndexList: List<Any?>,
    onOptionSelected: (Int, Int) -> Unit,
    onBlanksSelected: (Int, Map<String, String?>) -> Unit,
    questions: List<Question?>,
    showErrorMessage: (Int) -> Unit,
    blankQuestionContents: List<Map<String, Any>?>,
    blankWords: List<Map<String, Any>>,
    removeBlankContent: (Int) -> Unit,
    addBlankContent: (Int) -> Unit,
    getBlankQuestionAnswer: () -> Map<String, String?>,
) {
    HorizontalPager(
        state = rememberPagerState(
            initialPage = currentPage,
            pageCount = { questions.size },
        ),
        userScrollEnabled = false,
    ) {
        when (val currentQuestion = questions[currentPage]) {
            is ChoiceQuestion -> {
                ChoiceQuestionContent(
                    isOwner = isOwner,
                    isRealTime = isRealTime,
                    question = currentQuestion,
                    selectedIndex = selectedIndexList[currentPage] as? Int ?: -1,
                    onOptionSelected = { newIndex ->
                        onOptionSelected(currentPage, newIndex)
                    },
                )
            }

            is BlankQuestion -> {
                BlankQuestionContent(
                    isOwner = isOwner,
                    isRealTime = isRealTime,
                    contents = blankQuestionContents,
                    questionTitle = currentQuestion.title,
                    blankWords = blankWords,
                    removeBlankWord = { index ->
                        removeBlankContent(index)
                        onBlanksSelected(currentPage, getBlankQuestionAnswer())
                    },
                    addBlankWord = { index ->
                        addBlankContent(index)
                        onBlanksSelected(currentPage, getBlankQuestionAnswer())
                    },
                )
            }

            null -> {
                Log.e("QuizContent", "Question is null")
                showErrorMessage(R.string.err_load_questions)
            }
        }
    }
}


class QuizContentPreviewParameterProvider : PreviewParameterProvider<Question> {
    override val values = sequenceOf(
        BlankQuestion(
            id = "1",
            title = "문제 제목",
            questionContent = listOf(
                mapOf("text" to "바나나", "type" to "blank"),
                mapOf("text" to "는 원래 하얗다", "type" to "text"),
            ),
            solution = "문제 해설",
            userAnswers = emptyList(),
        ),
        ChoiceQuestion(
            id = "2",
            "문제 제목",
            description = "문제 설명",
            solution = "문제 해설",
            answer = 0,
            choices = listOf("객관식 1", "객관식 2", "객관식 3", "객관식 4"),
            userAnswers = emptyList(),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizContentPreview(
    @PreviewParameter(QuizContentPreviewParameterProvider::class) question : Question,
    ) {
    WeQuizTheme{
        QuizContent(
            isOwner = true,
            isRealTime = true,
            currentPage = 0,
            selectedIndexList = listOf(0, 1, 2),
            onOptionSelected = { _, _ -> },
            onBlanksSelected = { _, _ -> },
            questions = listOf(question),
            showErrorMessage = { },
            blankQuestionContents = listOf(
                mapOf("1" to "1", "2" to "2", "3" to "3"),
                mapOf("1" to "1", "2" to "2", "3" to "3"),
                mapOf("1" to "1", "2" to "2", "3" to "3"),
            ),
            blankWords = listOf(
                mapOf("1" to "1", "2" to "2", "3" to "3"),
                mapOf("1" to "1", "2" to "2", "3" to "3"),
                mapOf("1" to "1", "2" to "2", "3" to "3"),
            ),
            removeBlankContent = { },
            addBlankContent = { },
            getBlankQuestionAnswer = { mapOf("1" to "1", "2" to "2", "3" to "3") },
        )
    }

}

