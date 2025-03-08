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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.BlankQuestionDescription
import kr.boostcamp_2024.course.quiz.component.QuestionDescription
import kr.boostcamp_2024.course.quiz.component.QuestionDetailTopAppBar
import kr.boostcamp_2024.course.quiz.component.QuestionItems
import kr.boostcamp_2024.course.quiz.component.QuestionSolution
import kr.boostcamp_2024.course.quiz.component.QuestionTitle
import kr.boostcamp_2024.course.quiz.presentation.quiz.QuizStatisticsDialog
import kr.boostcamp_2024.course.quiz.utils.QuestionParameterProvider
import kr.boostcamp_2024.course.quiz.viewmodel.QuestionDetailViewModel

@Composable
internal fun QuestionDetailScreen(
    onNavigationButtonClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowErrorSnackbar: (Throwable) -> Unit,
    viewModel: QuestionDetailViewModel = hiltViewModel<QuestionDetailViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collectLatest { throwable -> onShowErrorSnackbar(throwable) }
    }

    QuestionDetailScreen(
        question = uiState.question,
        onNavigationButtonClick = onNavigationButtonClick,
        snackbarHostState = snackbarHostState,
    )

    if (uiState.isLoading) {
        WeQuizCircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuestionDetailScreen(
    question: Question?,
    onNavigationButtonClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { QuestionDetailTopAppBar(onNavigationButtonClick = onNavigationButtonClick) },
        floatingActionButton = {
            if (question is ChoiceQuestion) {
                ExtendedFloatingActionButton(
                    onClick = { showDialog = true },
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
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                } else if (question is BlankQuestion) {
                    BlankQuestionDescription(question.questionContent)
                }

                question.solution?.let {
                    QuestionSolution(question.solution)
                }
            }

            if (showDialog) {
                QuizStatisticsDialog(
                    onConfirmButtonClick = { showDialog = false },
                    onDismissRequest = { showDialog = false },
                    userAnswer = (question as ChoiceQuestion).userAnswers,
                )
            }
        }
    }
}

@PreviewKoLightDark
@Composable
private fun QuestionDetailScreenPreview(
    @PreviewParameter(QuestionParameterProvider::class) question: Question,
) {
    WeQuizTheme {
        QuestionDetailScreen(
            onNavigationButtonClick = {},
            question = question,
        )
    }
}
