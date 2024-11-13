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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.Question
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.presentation.viewmodel.QuestionViewModel
import kr.boostcamp_2024.course.quiz.utils.timerFormat
import kr.boostcamp_2024.course.quiz.viewmodel.QuizViewModel

@Composable
fun QuestionScreen(
    viewModel: QuizViewModel = hiltViewModel<QuizViewModel>(),
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: () -> Unit,
) {

    val quizState by viewModel.quizState.collectAsStateWithLifecycle()

    QuestionScreen(
        questions = quizState.questions,
        quizTitle = quizState.title,
        solveTime = quizState.solveTime,
        onNavigationButtonClick = onNavigationButtonClick,
        onQuizFinished = onQuizFinished,
    )
}

@Composable
fun QuestionScreen(
    questions: List<String>,
    quizTitle: String,
    solveTime: Int,
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: () -> Unit,
) {
    val questionViewModel = hiltViewModel<QuestionViewModel>()
    val uiState by questionViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        questionViewModel.initialize(solveTime, questions)
        questionViewModel.updateTimer()
    }
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn {
            item {
                QuestionTopBar(title = quizTitle) { questionViewModel.toggleDialog(true) }
            }
            item {
                LinearProgressIndicator(
                    progress = { (questionViewModel.uiState.value.currentPage + 1) / questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                ) {
                    WeQuizRightChatBubble(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "${stringResource(R.string.txt_question_timer)} ${timerFormat(uiState.countDownTime)}",
                    )
                    WeQuizLocalRoundedImage(
                        modifier = Modifier.size(120.dp).align(Alignment.CenterVertically),
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
                        QuestionTitleAndDetail(
                            title = uiState.questions[uiState.currentPage].title,
                            description = uiState.questions[uiState.currentPage].description,
                        )
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
                    text = if (uiState.currentPage == questions.size - 1) {
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
            title = if (uiState.currentPage == questions.size - 1) {
                stringResource(R.string.dialog_submit_script)
            } else {
                stringResource(R.string.dialog_exit_script)
            },
            confirmTitle = if (uiState.currentPage == questions.size - 1) {
                stringResource(R.string.txt_question_submit)
            } else {
                stringResource(R.string.txt_question_exit)
            },
            dismissTitle = stringResource(R.string.txt_question_cancel),
            onConfirm = {
                questionViewModel.toggleDialog(false)
                if (uiState.currentPage == questions.size - 1) {
                    /*
                    TODO (제출 버튼 눌렀을 때)
                     네트워크 통신 진행 - selectedIndexList 전달
                     통신 완료 후 onQuizFinished() 실행(isSubmitting = false로 변경)
                     */
                    onQuizFinished()
                } else {
                    /*
                    TODO (나가기 버튼 눌렀을 때)
                     네트워크 통신 진행 - selectedIndexList 전달
                     통신 완료 후 onNavigationButtonClick() 실행(isSubmitting = false로 변경)
                     */
                    onNavigationButtonClick()
                }
            },
            onDismissRequest = { questionViewModel.toggleDialog(false) },
            dialogImage = painterResource(id = R.drawable.quiz_system_profile),
            content = { },
        )
    }
    if (uiState.errorMessage.isBlank()) {
        LaunchedEffect(uiState.errorMessage) {
            snackbarHostState.showSnackbar(uiState.errorMessage)
            questionViewModel.shownErrorMessage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    QuestionScreen(
        questions = listOf(
            "OJSAMgoMx4mwuwyg9JLo",
            "AP60qaPeHDfwJ7OGZygb",
            "VNsXOZvL9K85Nl0j8B00",
            "4TiUsbbveB7ruRVxPGae",
            "acLeak1Zooy6RIvyOOXc",
        ),
        quizTitle = "뿌셔뿌셔 불고기",
        solveTime = 70,
        onNavigationButtonClick = {},
        onQuizFinished = {},
    )
}
