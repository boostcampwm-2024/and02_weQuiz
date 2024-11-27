package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.quiz.viewmodel.QuestionViewModel

@Composable
fun QuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: (String?, String?) -> Unit,
    questionViewModel: QuestionViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by questionViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.quiz is RealTimeQuiz) {
        val quiz = uiState.quiz as RealTimeQuiz

        if (quiz.ownerId == uiState.currentUserId) {
            OwnerQuestionScreen(
                quiz = quiz,
                currentUserId = uiState.currentUserId,
                onQuizFinished = { _, quizId -> onQuizFinished(null, quizId) },
            )
        } else {
            UserQuestionScreen(
                onNavigationButtonClick = onNavigationButtonClick,
                onQuizFinished = { userOmrId, _ ->
                    onQuizFinished(userOmrId, null)
                },
            )
        }
    } else if (uiState.quiz is Quiz) {
        uiState.countDownTime?.let { currentCountDownTime ->
            GeneralQuestionScreen(
                quiz = uiState.quiz,
                currentPage = uiState.currentPage,
                questions = uiState.questions,
                countDownTime = currentCountDownTime,
                selectedIndexList = uiState.selectedIndexList,
                snackbarHostState = snackbarHostState,
                onOptionSelected = questionViewModel::selectOption,
                onNextButtonClick = questionViewModel::nextPage,
                onPreviousButtonClick = questionViewModel::previousPage,
                onSubmitButtonClick = questionViewModel::submitAnswers,
                onNavigationButtonClick = onNavigationButtonClick,
                showErrorMessage = questionViewModel::showErrorMessage,
                onBlanksSelected = questionViewModel::selectBlanks,
                blankQuestionContents = uiState.blankQuestionContents,
                blankWords = uiState.blankWords,
                removeBlankContent = questionViewModel.blankQuestionManager::removeBlankContent,
                addBlankContent = questionViewModel.blankQuestionManager::addBlankContent,
                getBlankQuestionAnswer = questionViewModel.blankQuestionManager::getAnswer,
            )
        }
    }

    uiState.errorMessageId?.let { errorMessageId ->
        val errorMessage = stringResource(errorMessageId)
        LaunchedEffect(errorMessageId) {
            snackbarHostState.showSnackbar(errorMessage)
            questionViewModel.shownErrorMessage()
        }
    }

    uiState.userOmrId?.let { userOmrId ->
        LaunchedEffect(userOmrId) {
            onQuizFinished(userOmrId, null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    QuestionScreen(
        onNavigationButtonClick = {},
        onQuizFinished = { _, _ -> },
    )
}
