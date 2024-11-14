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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.Question
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.presentation.viewmodel.QuestionViewModel
import kr.boostcamp_2024.course.quiz.utils.timerFormat

@Composable
fun QuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: () -> Unit,
) {

    QuestionScreen(
        questionViewModel = hiltViewModel(),
        onNavigationButtonClick = onNavigationButtonClick,
        onQuizFinished = onQuizFinished,
    )
}

@Composable
fun QuestionScreen(
    questionViewModel: QuestionViewModel = hiltViewModel(),
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: () -> Unit,
) {
    val uiState by questionViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { uiState.quiz?.let { QuestionTopBar(title = it.title) { questionViewModel.toggleDialog(true) } } },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
            ) {
                LazyColumn {
                    item {
                        LinearProgressIndicator(
                            progress = { (questionViewModel.uiState.value.currentPage + 1) / uiState.questions.size.toFloat() },
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
                            WeQuizRightChatBubble(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = "${stringResource(R.string.txt_question_timer)} ${timerFormat(uiState.countDownTime)}",
                            )
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
                                initialPage = uiState.currentPage,
                                pageCount = { uiState.questions.size },
                            ),
                            userScrollEnabled = false,
                        ) {
                            Column {
                                uiState.questions[uiState.currentPage].description.let { description ->
                                    QuestionTitleAndDetail(
                                        title = uiState.questions[uiState.currentPage].title,
                                        description = description,
                                    )
                                }
                                Question(
                                    questions = uiState.questions[uiState.currentPage].choices,
                                    selectedIndex = uiState.selectedIndexList[uiState.currentPage],
                                    onOptionSelected = { newIndex ->
                                        questionViewModel.selectOption(uiState.currentPage, newIndex)
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
                            if (uiState.currentPage < uiState.questions.size - 1) {
                                questionViewModel.nextPage()
                            } else {
                                questionViewModel.toggleDialog(true)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = if (uiState.currentPage == uiState.questions.size - 1) {
                                stringResource(R.string.txt_question_done)
                            } else {
                                stringResource(R.string.txt_question_next_question)
                            },
                        )
                    }
                    Button(
                        onClick = {
                            if (uiState.currentPage > 0) questionViewModel.previousPage()
                        },
                        enabled = uiState.currentPage > 0,
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

            if (uiState.showDialog) {
                WeQuizBaseDialog(
                    title = if (uiState.currentPage == uiState.questions.size - 1) {
                        stringResource(R.string.dialog_submit_script)
                    } else {
                        stringResource(R.string.dialog_exit_script)
                    },
                    confirmTitle = if (uiState.currentPage == uiState.questions.size - 1) {
                        stringResource(R.string.txt_question_submit)
                    } else {
                        stringResource(R.string.txt_question_exit)
                    },
                    dismissTitle = stringResource(R.string.txt_question_cancel),
                    onConfirm = {
                        questionViewModel.toggleDialog(false)
                        if (uiState.currentPage == uiState.questions.size - 1) {
                            questionViewModel.submitAnswers()
                            onQuizFinished()
                        } else {
                            onNavigationButtonClick()
                        }
                    },
                    onDismissRequest = { questionViewModel.toggleDialog(false) },
                    dialogImage = painterResource(id = R.drawable.quiz_system_profile),
                    content = { },
                )
            }
            if (uiState.errorMessageId != null) {
                LaunchedEffect(uiState.errorMessageId) {
                    snackbarHostState.showSnackbar(context.getString(uiState.errorMessageId!!))
                    questionViewModel.shownErrorMessage()
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    QuestionScreen(
        onNavigationButtonClick = {},
        onQuizFinished = {},
    )
}
