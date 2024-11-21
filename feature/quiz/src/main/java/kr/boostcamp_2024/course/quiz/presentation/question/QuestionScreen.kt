package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.presentation.viewmodel.QuestionViewModel

@Composable
fun QuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: (String) -> Unit,
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
                onNavigationButtonClick = onNavigationButtonClick,
                onQuizFinished = onQuizFinished,
            )
        } else {
            UserQuestionScreen(
                onNavigationButtonClick = onNavigationButtonClick,
                onQuizFinished = onQuizFinished,
            )
        }
    } else {
        GeneralQuestionScreen(
            quiz = uiState.quiz,
            currentPage = uiState.currentPage,
            questions = uiState.questions,
            countDownTime = uiState.countDownTime,
            selectedIndexList = uiState.selectedIndexList,
            snackbarHostState = snackbarHostState,
            onOptionSelected = questionViewModel::selectOption,
            onNextButtonClick = questionViewModel::nextPage,
            onPreviousButtonClick = questionViewModel::previousPage,
            onSubmitButtonClick = questionViewModel::submitAnswers,
            onNavigationButtonClick = onNavigationButtonClick,
        )
    }

    uiState.errorMessageId?.let { errorMessageId ->
        val errorMessage = stringResource(R.string.err_answer_add)
        LaunchedEffect(errorMessageId) {
            snackbarHostState.showSnackbar(errorMessage)
            questionViewModel.shownErrorMessage()
        }
    }

    uiState.userOmrId?.let { userOmrId ->
        LaunchedEffect(userOmrId) {
            onQuizFinished(userOmrId)
        }
    }
}
