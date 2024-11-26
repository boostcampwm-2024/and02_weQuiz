package kr.boostcamp_2024.course.quiz.component

import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionManager
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.R

@Composable
fun QuizContent(
    isOwner: Boolean = false,
    isRealTime: Boolean = false,
    currentPage: Int,
    selectedIndexList: List<Int>,
    onOptionSelected: (Int, Int) -> Unit,
    onBlanksSelected: (Map<Int, String?>) -> Unit,
    questions: List<Question?>,
    showErrorMessage: (Int) -> Unit,
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
                    selectedIndex = selectedIndexList[currentPage],
                    onOptionSelected = { newIndex ->
                        onOptionSelected(currentPage, newIndex)
                    },
                )
            }
            is BlankQuestion -> {
                val blankQuestionManager = BlankQuestionManager(currentQuestion.questionContent)
                BlankQuestionContent(
                    isOwner = isOwner,
                    isRealTime = isRealTime,
                    contents = blankQuestionManager.contents,
                    questionTitle = currentQuestion.title,
                    blankWords = blankQuestionManager.blankWords,
                    removeBlankWord = { index ->
                        blankQuestionManager.removeBlankContent(index)
                        onBlanksSelected(blankQuestionManager.getAnswer())
                    },
                    addBlankWord = { index ->
                        blankQuestionManager.addBlankContent(index)
                        onBlanksSelected(blankQuestionManager.getAnswer())
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
