package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.component.UserQuestion
import kr.boostcamp_2024.course.quiz.presentation.viewmodel.UserQuestionViewModel

@Composable
fun UserQuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: (String?, String?) -> Unit,
    userQuestionViewModel: UserQuestionViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by userQuestionViewModel.uiState.collectAsStateWithLifecycle()
    var quizFinishDialog by rememberSaveable { mutableStateOf(false) }

    UserQuestionScreen(
        quiz = uiState.quiz,
        currentPage = uiState.currentPage,
        choiceQuestions = uiState.choiceQuestions,
        quizFinishDialog = quizFinishDialog,
        onQuizFinishDialogDismissButtonClick = { quizFinishDialog = false },
        selectedIndexList = uiState.selectedIndexList,
        snackbarHostState = snackbarHostState,
        onOptionSelected = userQuestionViewModel::selectOption,
        onNavigationButtonClick = onNavigationButtonClick,
        onSubmitButtonClick = userQuestionViewModel::submitQuestion,
        isSubmitted = uiState.isSubmitted,
        onQuizFinishButtonClick = userQuestionViewModel::submitAnswers,
    )

    uiState.errorMessageId?.let { errorMessageId ->
        val errorMessage = stringResource(errorMessageId)
        LaunchedEffect(errorMessageId) {
            snackbarHostState.showSnackbar(errorMessage)
            userQuestionViewModel.shownErrorMessage()
        }
    }

    uiState.userOmrId?.let { userOmrId ->
        LaunchedEffect(userOmrId) {
            onQuizFinished(userOmrId, null)
        }
    }

    if (uiState.isQuizFinished) {
        quizFinishDialog = true
    }
}

@Composable
fun UserQuestionScreen(
    quiz: BaseQuiz?,
    currentPage: Int,
    choiceQuestions: List<ChoiceQuestion>,
    quizFinishDialog: Boolean,
    onQuizFinishDialogDismissButtonClick: () -> Unit,
    selectedIndexList: List<Int>,
    snackbarHostState: SnackbarHostState,
    onOptionSelected: (Int, Int) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onSubmitButtonClick: (String) -> Unit,
    isSubmitted: Boolean,
    onQuizFinishButtonClick: () -> Unit,
) {
    var showExitDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            quiz?.let {
                QuestionTopBar(
                    title = it.title,
                    onShowDialog = { showExitDialog = true },
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        ) {
            LazyColumn {
                item {
                    LinearProgressIndicator(
                        progress = { (currentPage + 1) / choiceQuestions.size.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    HorizontalPager(
                        state = rememberPagerState(
                            initialPage = currentPage,
                            pageCount = { choiceQuestions.size },
                        ),
                        userScrollEnabled = false,
                    ) {
                        Column {
                            QuestionTitleAndDetail(
                                title = choiceQuestions[currentPage].title,
                                description = choiceQuestions[currentPage].description,
                            )

                            UserQuestion(
                                questions = choiceQuestions[currentPage].choices,
                                selectedIndex = selectedIndexList[currentPage],
                                onOptionSelected = { newIndex ->
                                    onOptionSelected(currentPage, newIndex)
                                },
                                enable = !isSubmitted,
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            onSubmitButtonClick(choiceQuestions[currentPage].id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        enabled = !isSubmitted,
                    ) {
                        Text(if (isSubmitted) stringResource(R.string.btn_success_submit) else stringResource(R.string.btn_submit))
                    }
                }
            }
        }
    }

    if (quizFinishDialog) {
        WeQuizBaseDialog(
            title = stringResource(R.string.txt_quiz_finish_notification),
            dialogImage = painterResource(id = R.drawable.quiz_system_profile),
            confirmTitle = stringResource(R.string.txt_quiz_finish_confirm),
            onConfirm = onQuizFinishButtonClick,
            onDismissRequest = onQuizFinishDialogDismissButtonClick,
            dismissButton = null,
            content = { /* no-op */ },
        )
    }

    if (showExitDialog) {
        WeQuizBaseDialog(
            title = if (currentPage == choiceQuestions.size - 1) {
                stringResource(R.string.dialog_submit_script)
            } else {
                stringResource(R.string.dialog_exit_script)
            },
            confirmTitle = if (currentPage == choiceQuestions.size - 1) {
                stringResource(R.string.txt_question_submit)
            } else {
                stringResource(R.string.txt_question_exit)
            },
            dismissTitle = stringResource(R.string.txt_question_cancel),
            onConfirm = {
                showExitDialog = false
                onNavigationButtonClick()
            },
            onDismissRequest = { showExitDialog = false },
            dialogImage = painterResource(id = R.drawable.quiz_system_profile),
            content = { /* no-op */ },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserQuestionScreenPreview() {
    UserQuestionScreen(
        onNavigationButtonClick = {},
        onQuizFinished = { _, _ -> },
    )
}
