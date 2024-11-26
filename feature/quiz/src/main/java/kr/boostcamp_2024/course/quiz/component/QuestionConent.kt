package kr.boostcamp_2024.course.quiz.component

import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.R

@Composable
fun QuizContent(
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
