package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.quiz.component.QuizDataButton
import kr.boostcamp_2024.course.quiz.component.QuizDataChip
import kr.boostcamp_2024.course.quiz.component.QuizDataText
import kr.boostcamp_2024.course.quiz.component.QuizTopAppBar
import kr.boostcamp_2024.course.quiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: (String) -> Unit,
    onSettingMenuClick: (String, String) -> Unit,
    onQuizDeleteSuccess: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.value.isDeleteQuizSuccess) {
        LaunchedEffect(Unit) {
            onQuizDeleteSuccess()
        }
    }

    QuizScreen(
        category = uiState.value.category,
        quiz = uiState.value.quiz,
        snackbarHostState = snackbarHostState,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateQuestionButtonClick = onCreateQuestionButtonClick,
        onStartQuizButtonClick = onStartQuizButtonClick,
        onSettingMenuClick = onSettingMenuClick,
        onDeleteMenuClick = viewModel::deleteQuiz,
    )

    if (uiState.value.isLoading) {
        WeQuizCircularProgressIndicator()
    }

    uiState.value.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.shownErrorMessage()
        }
    }
}

@Composable
fun QuizScreen(
    category: Category?,
    quiz: BaseQuiz?,
    snackbarHostState: SnackbarHostState,
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: (String) -> Unit,
    onSettingMenuClick: (String, String) -> Unit,
    onDeleteMenuClick: (String, BaseQuiz) -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            QuizTopAppBar(
                category = category,
                quiz = quiz,
                onNavigationButtonClick = onNavigationButtonClick,
                onSettingMenuClick = onSettingMenuClick,
                onDeleteMenuClick = onDeleteMenuClick,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        bottom = innerPadding.calculateBottomPadding(),
                    ),
                ),
        ) {

            WeQuizAsyncImage(
                modifier = Modifier.fillMaxSize(),
                imgUrl = quiz?.quizImageUrl,
                contentDescription = null,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                // QuizTitle & QuizDescription
                QuizDataText(
                    quiz = quiz,
                )

                // QuizChip
                QuizDataChip(
                    category = category,
                    quiz = quiz,
                )

                // QuizButton
                QuizDataButton(
                    quiz = quiz,
                    isOwner = true,
                    isWaiting = false,
                    onCreateQuestionButtonClick = onCreateQuestionButtonClick,
                    onStartQuizButtonClick = onStartQuizButtonClick,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizStartScreenPreview() {
    WeQuizTheme {
        QuizScreen(
            category = Category(
                id = "id",
                name = "name",
                description = "description",
                categoryImageUrl = "categoryImageUrl",
                quizzes = emptyList(),
            ),
            quiz = Quiz(
                id = "id",
                title = "퀴즈 제목임",
                description = "퀴즈 설명임",
                startTime = "startTime",
                solveTime = 60,
                questions = emptyList(),
                userOmrs = emptyList(),
                quizImageUrl = "quizImageUrl",
            ),
            snackbarHostState = SnackbarHostState(),
            onNavigationButtonClick = {},
            onCreateQuestionButtonClick = {},
            onStartQuizButtonClick = {},
            onSettingMenuClick = { _, _ -> },
            onDeleteMenuClick = { _, _ -> },
        )
    }
}
