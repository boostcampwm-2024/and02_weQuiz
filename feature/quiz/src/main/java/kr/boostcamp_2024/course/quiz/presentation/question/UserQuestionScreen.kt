package kr.boostcamp_2024.course.quiz.presentation.question

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
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.Question
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.presentation.viewmodel.UserQuestionViewModel

@Composable
fun UserQuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: (String) -> Unit,
    userQuestionViewModel: UserQuestionViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by userQuestionViewModel.uiState.collectAsStateWithLifecycle()

    UserQuestionScreen(
        quiz = uiState.quiz,
        currentPage = uiState.currentPage,
        questions = uiState.questions,
//        countDownTime = uiState.countDownTime,
        selectedIndexList = uiState.selectedIndexList,
        snackbarHostState = snackbarHostState,
        onOptionSelected = userQuestionViewModel::selectOption,
//        onNextButtonClick = userQuestionViewModel::nextPage,
        onNextByOwner = userQuestionViewModel::nextPage, //신규 생성
//        onPreviousButtonClick = userQuestionViewModel::previousPage,
//        onSubmitButtonClick = userQuestionViewModel::submitAnswers,
        onSubmitAnsewersByOwner = userQuestionViewModel::submitAnswers, //신규 생성
        onNavigationButtonClick = onNavigationButtonClick,
        onSubmitButtonClick = userQuestionViewModel::submitAnswers,
    )

    uiState.errorMessageId?.let { errorMessageId ->
        val errorMessage = stringResource(R.string.err_answer_add)
        LaunchedEffect(errorMessageId) {
            snackbarHostState.showSnackbar(errorMessage)
            userQuestionViewModel.shownErrorMessage()
        }
    }

    uiState.userOmrId?.let { userOmrId ->
        LaunchedEffect(userOmrId) {
            onQuizFinished(userOmrId)
        }
    }
}

@Composable
fun UserQuestionScreen(
    quiz: BaseQuiz?,
    currentPage: Int,
    questions: List<Question>,
//    countDownTime: Int,
    selectedIndexList: List<Int>,
    snackbarHostState: SnackbarHostState,
    onOptionSelected: (Int, Int) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onNextByOwner: () -> Unit,
//    onPreviousButtonClick: () -> Unit,
    onSubmitAnsewersByOwner: () -> Unit,
    onSubmitButtonClick: () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            quiz?.let {
                QuestionTopBar(
                    title = it.title,
                    onShowDialog = { showDialog = true },
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
                        progress = { (currentPage + 1) / questions.size.toFloat() },
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
                            pageCount = { questions.size },
                        ),
                        userScrollEnabled = false,
                    ) {
                        Column {
                            QuestionTitleAndDetail(
                                title = questions[currentPage].title,
                                description = questions[currentPage].description,
                            )

                            Question(
                                questions = questions[currentPage].choices,
                                selectedIndex = selectedIndexList[currentPage],
                                onOptionSelected = { newIndex ->
                                    onOptionSelected(currentPage, newIndex)
                                },
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            if (currentPage < questions.size - 1) {
                                onSubmitButtonClick()
                            }
//                            } else {
//                                showDialog = true
//                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(
                            "제출",
//                            text = if (currentPage == questions.size - 1) {
//                                stringResource(R.string.txt_question_done)
//                            } else {
//                                stringResource(R.string.txt_question_next_question)
//                            },
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        WeQuizBaseDialog(
            title = if (currentPage == questions.size - 1) {
                stringResource(R.string.dialog_submit_script)
            } else {
                stringResource(R.string.dialog_exit_script)
            },
            confirmTitle = if (currentPage == questions.size - 1) {
                stringResource(R.string.txt_question_submit)
            } else {
                stringResource(R.string.txt_question_exit)
            },
            dismissTitle = stringResource(R.string.txt_question_cancel),
            onConfirm = {
                showDialog = false
                if (currentPage == questions.size - 1) {
                    onSubmitAnsewersByOwner()
                } else {
                    onNavigationButtonClick()
                }
            },
            onDismissRequest = { showDialog = false },
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
        onQuizFinished = {},
    )
}
