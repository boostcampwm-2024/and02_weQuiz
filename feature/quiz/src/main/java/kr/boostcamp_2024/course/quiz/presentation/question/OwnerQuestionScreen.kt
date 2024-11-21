package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.Question
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.presentation.viewmodel.QuestionViewModel

@Composable
fun OwnerQuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: (String) -> Unit,
    questionViewModel: QuestionViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by questionViewModel.uiState.collectAsStateWithLifecycle()

    OwnerQuestionScreen(
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

@Composable
fun OwnerQuestionScreen(
    quiz: BaseQuiz?,
    currentPage: Int,
    questions: List<Question>,
    countDownTime: Int,
    selectedIndexList: List<Int>,
    snackbarHostState: SnackbarHostState,
    onNavigationButtonClick: () -> Unit,
    onOptionSelected: (Int, Int) -> Unit,
    onNextButtonClick: () -> Unit,
    onPreviousButtonClick: () -> Unit,
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                        ) {
                            WeQuizRightChatBubble(
                                modifier = Modifier,
                                text = "진행자: 홍길동 ",
                            )
                            WeQuizRightChatBubble(
                                modifier = Modifier,
                                text = "제출 7/10명",
                            )
                        }
                        WeQuizLocalRoundedImage(
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.CenterVertically),
                            imagePainter = painterResource(id = R.drawable.quiz_system_profile),
                            contentDescription = stringResource(R.string.des_image_question),
                        )
                    }
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
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Button(
                    onClick = {
                        if (currentPage < questions.size - 1) {
                            onNextButtonClick()
                        } else {
                            showDialog = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (currentPage == questions.size - 1) {
                            stringResource(R.string.txt_question_done)
                        } else {
                            stringResource(R.string.txt_question_next_question)
                        },
                    )
                }
                Button(
                    onClick = {
                        if (currentPage > 0) onPreviousButtonClick()
                    },
                    enabled = currentPage > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                    ),
                ) {
                    Text(text = stringResource(R.string.btn_prev_question))
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
                        onSubmitButtonClick()
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
}

@Preview(showBackground = true)
@Composable
fun OwnerQuestionScreenPreview() {
    OwnerQuestionScreen(
        onNavigationButtonClick = {},
        onQuizFinished = {},
    )
}
