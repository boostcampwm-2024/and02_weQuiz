package kr.boostcamp_2024.course.quiz.presentation.quiz

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.viewmodel.QuizResultViewModel

@Composable
fun QuizResultScreen(
    onNavigationButtonClick: () -> Unit,
    onQuestionClick: (String) -> Unit,
    quizResultViewModel: QuizResultViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by quizResultViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isManager) {
        OwnerQuizResultScreen(
            questions = uiState.questions,
            quizTitle = uiState.quizTitle,
            snackbarHostState = snackbarHostState,
            onNavigationButtonClick = onNavigationButtonClick,
            onQuestionClick = onQuestionClick,
        )
    } else {
        GeneralQuizResultScreen(
            quizTitle = uiState.quizTitle,
            questions = uiState.questions,
            quizResult = uiState.quizResult,
            onNavigationButtonClick = onNavigationButtonClick,
            snackbarHostState = snackbarHostState,
            onQuestionClick = onQuestionClick,
        )
    }

    if (uiState.isLoading) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
            )
        }
    }

    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            quizResultViewModel.shownErrorMessage()
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(showBackground = true)
@Composable
fun QuizResultScreenPreview() {
    WeQuizTheme {
        QuizResultScreen(
            onNavigationButtonClick = {},
            onQuestionClick = {},
        )
    }
}
