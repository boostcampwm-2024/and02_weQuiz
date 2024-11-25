package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.component.QuestionDescription
import kr.boostcamp_2024.course.quiz.component.QuestionDetailTopAppBar
import kr.boostcamp_2024.course.quiz.component.QuestionItems
import kr.boostcamp_2024.course.quiz.component.QuestionSolution
import kr.boostcamp_2024.course.quiz.component.QuestionTitle
import kr.boostcamp_2024.course.quiz.viewmodel.QuestionDetailViewModel

@Composable
fun QuestionDetailScreen(
    viewModel: QuestionDetailViewModel = hiltViewModel<QuestionDetailViewModel>(),
    onNavigationButtonClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuestionDetailScreen(
        question = uiState.question,
        errorMessage = uiState.errorMessage,
        onNavigationButtonClick = onNavigationButtonClick,
        onErrorMessageShown = viewModel::shownErrorMessage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionDetailScreen(
    question: Question?,
    errorMessage: String?,
    onNavigationButtonClick: () -> Unit,
    onErrorMessageShown: () -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { QuestionDetailTopAppBar(onNavigationButtonClick = onNavigationButtonClick) },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 10.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            question?.let {
                QuestionTitle(question.title)

                if (question is ChoiceQuestion) {
                    QuestionDescription(question.description)
                    QuestionItems(question.choices, question.answer) {}
                } else {
                    // TODO: blank question 처리 해야 해요!!
                }

                QuestionSolution(question.solution)
            }
        }
        if (errorMessage != null) {
            LaunchedEffect(errorMessage) {
                snackBarHostState.showSnackbar(errorMessage)
                onErrorMessageShown()
            }
        }
    }
}

@Preview
@Composable
fun QuestionDetailScreenPreview() {
    WeQuizTheme {
        QuestionDetailScreen(
            onNavigationButtonClick = {},
            question = BlankQuestion(
                id = "1",
                title = "문제 제목",
                questionContent = emptyList(),
                solution = "문제 해설",
                type = "blank",
            ),
            errorMessage = null,
        )
    }
}
