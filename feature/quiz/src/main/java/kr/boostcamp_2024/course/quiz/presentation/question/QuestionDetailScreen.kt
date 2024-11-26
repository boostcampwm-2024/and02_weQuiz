package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.QuestionDescription
import kr.boostcamp_2024.course.quiz.component.QuestionDetailTopAppBar
import kr.boostcamp_2024.course.quiz.component.QuestionItems
import kr.boostcamp_2024.course.quiz.component.QuestionSolution
import kr.boostcamp_2024.course.quiz.component.QuestionTitle
import kr.boostcamp_2024.course.quiz.presentation.quiz.QuizStatisticsDialog
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
        userAnswer = uiState.userAnswer,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionDetailScreen(
    question: Question?,
    errorMessage: String?,
    onNavigationButtonClick: () -> Unit,
    onErrorMessageShown: () -> Unit = {},
    userAnswer: List<Int>,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { QuestionDetailTopAppBar(onNavigationButtonClick = onNavigationButtonClick) },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier.padding(end = 16.dp, bottom = 53.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = MaterialTheme.shapes.large,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.fab_quiz_result),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.txt_quiz_result),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        },
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

            if (showDialog) {
                QuizStatisticsDialog(
                    onConfirmButtonClick = { showDialog = false },
                    onDismissRequest = { showDialog = false },
                    userAnswer = userAnswer,
                )
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
            userAnswer = listOf(0, 0, 0, 0),
        )
    }
}
