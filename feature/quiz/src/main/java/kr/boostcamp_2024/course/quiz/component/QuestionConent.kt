package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDarkBackground
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.utils.QuizContentPreviewParameterProvider

@Composable
internal fun QuizContent(
    isOwner: Boolean = false,
    isRealTime: Boolean = false,
    currentPage: Int,
    selectedIndexList: List<Any?>,
    onOptionSelected: (Int, Int) -> Unit,
    onBlanksSelected: (Int, Map<String, String?>) -> Unit,
    questions: List<Question?>,
    blankQuestionContents: List<Map<String, Any>?>,
    blankWords: List<Map<String, Any>>,
    removeBlankContent: (Int) -> Unit,
    addBlankContent: (Int) -> Unit,
    getBlankQuestionAnswer: () -> Map<String, String?>,
    onShowErrorSnackbar: (Throwable) -> Unit,
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

            else -> {
                // TODO 재시도 처리(?)
                onShowErrorSnackbar(Exception("Question is null"))
            }
        }
    }
}

@PreviewKoLightDarkBackground
@Composable
private fun QuizContentPreview(
    @PreviewParameter(QuizContentPreviewParameterProvider::class) question: Question,
) {
    WeQuizTheme {
        QuizContent(
            isOwner = true,
            isRealTime = true,
            currentPage = 0,
            selectedIndexList = listOf(0, 1, 2),
            onOptionSelected = { _, _ -> },
            onBlanksSelected = { _, _ -> },
            questions = listOf(question),
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
            onShowErrorSnackbar = {},
        )
    }

}
