package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.QuizResult
import kr.boostcamp_2024.course.quiz.viewmodel.QuizResultViewModel

@Composable
fun QuizResultScreen(
    onNavigationButtonClick: () -> Unit,
    onQuestionClick: (String) -> Unit,
    quizResultViewModel: QuizResultViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by quizResultViewModel.uiState.collectAsStateWithLifecycle()
    QuizResultScreen(
        isManager = uiState.isManager,
        isLoading = uiState.isLoading,
        questions = uiState.questions,
        quizTitle = uiState.quizTitle,
        quizResult = uiState.quizResult,
        snackbarHostState = snackbarHostState,
        onNavigationButtonClick = onNavigationButtonClick,
        onQuestionClick = onQuestionClick,
    )

    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            quizResultViewModel.shownErrorMessage()
        }
    }
}

@Composable
fun QuizResultScreen(
    isManager: Boolean,
    isLoading: Boolean,
    questions: List<Question>?,
    quizTitle: String?,
    quizResult: QuizResult?,
    snackbarHostState: SnackbarHostState,
    onNavigationButtonClick: () -> Unit,
    onQuestionClick: (String) -> Unit,
) {
    if (isManager) {
        OwnerQuizResultScreen(
            questions = questions,
            quizTitle = quizTitle,
            snackbarHostState = snackbarHostState,
            onNavigationButtonClick = onNavigationButtonClick,
            onQuestionClick = onQuestionClick,
        )
    } else {
        GeneralQuizResultScreen(
            quizTitle = quizTitle,
            quizResult = quizResult,
            onNavigationButtonClick = onNavigationButtonClick,
            snackbarHostState = snackbarHostState,
            onQuestionClick = onQuestionClick,
        )
    }

    if (isLoading) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
            )
        }
    }
}
