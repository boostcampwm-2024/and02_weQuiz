package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.activity.compose.BackHandler
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
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.OwnerFinishQuizDialog
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.component.RealTimeQuestion
import kr.boostcamp_2024.course.quiz.viewmodel.OwnerQuestionViewModel

@Composable
fun OwnerQuestionScreen(
    quiz: RealTimeQuiz?,
    currentUserId: String?,
    onQuizFinished: (String?, String?) -> Unit,
    questionViewModel: OwnerQuestionViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by questionViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        questionViewModel.initQuizData(quiz, currentUserId)
    }

    OwnerQuestionScreen(
        quiz = uiState.quiz,
        currentPage = uiState.currentPage,
        questions = uiState.questions,
        ownerName = uiState.ownerName ?: "",
        snackbarHostState = snackbarHostState,
        onNextButtonClick = questionViewModel::nextPage,
        onPreviousButtonClick = questionViewModel::previousPage,
        onQuizFinishButtonClick = questionViewModel::setQuizFinished,
    )

    if (uiState.isQuizFinished) {
        onQuizFinished(null, requireNotNull(uiState.quiz?.id))
    }

    uiState.errorMessageId?.let { errorMessageId ->
        val errorMessage = stringResource(errorMessageId)
        LaunchedEffect(errorMessageId) {
            snackbarHostState.showSnackbar(errorMessage)
            questionViewModel.shownErrorMessage()
        }
    }
}

@Composable
fun OwnerQuestionScreen(
    quiz: RealTimeQuiz?,
    currentPage: Int,
    questions: List<Question?>,
    ownerName: String,
    snackbarHostState: SnackbarHostState,
    onNextButtonClick: () -> Unit,
    onPreviousButtonClick: () -> Unit,
    onQuizFinishButtonClick: () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val currentQuestion = questions.getOrNull(currentPage)

    BackHandler {
        showDialog = true
    }

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
            LinearProgressIndicator(
                progress = { (currentPage + 1) / questions.size.toFloat() },
                modifier = Modifier.fillMaxWidth(),
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (quiz != null && currentQuestion != null) {
                    item {
                        RealTimeQuizWithOwnerGuideContent(
                            ownerName = ownerName,
                            totalParticipants = quiz.waitingUsers.size,
                            submittedParticipants = currentQuestion.currentSubmit,
                        )
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
                                    title = currentQuestion.title,
                                    description = currentQuestion.description,
                                )

                                RealTimeQuestion(
                                    isOwner = true,
                                    questions = currentQuestion.choices,
                                    selectedIndex = currentQuestion.answer,
                                )
                            }
                        }
                    }
                }
            }

            RealTimeQuizWithOwnerButtons(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .padding(10.dp),
                prevButtonEnabled = currentPage > 0,
                nextButtonText = if (currentPage == questions.size - 1) {
                    stringResource(R.string.txt_question_done)
                } else {
                    stringResource(R.string.txt_question_next_question)
                },
                onNextButtonClick = {
                    if (currentPage < questions.size - 1) {
                        onNextButtonClick()
                    } else {
                        showDialog = true
                    }
                },
                onPrevButtonClick = {
                    if (currentPage > 0) onPreviousButtonClick()
                },
            )
        }

        if (showDialog) {
            OwnerFinishQuizDialog(
                onFinishQuizButtonClick = onQuizFinishButtonClick,
                onDismissButtonClick = { showDialog = false },
            )
        }
    }
}

@Composable
fun RealTimeQuizWithOwnerGuideContent(
    ownerName: String,
    totalParticipants: Int,
    submittedParticipants: Int,
) {
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
            modifier = Modifier,
        ) {
            WeQuizRightChatBubble(
                modifier = Modifier,
                text = stringResource(R.string.txt_quiz_owner, ownerName),
            )
            WeQuizRightChatBubble(
                modifier = Modifier,
                text = stringResource(R.string.txt_quiz_submit_state, submittedParticipants, totalParticipants),
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

@Composable
fun RealTimeQuizWithOwnerButtons(
    modifier: Modifier = Modifier,
    prevButtonEnabled: Boolean,
    nextButtonText: String,
    onNextButtonClick: () -> Unit,
    onPrevButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Button(
            onClick = onNextButtonClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = nextButtonText,
            )
        }
        Button(
            onClick = onPrevButtonClick,
            enabled = prevButtonEnabled,
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

@Preview(showBackground = true)
@Composable
fun OwnerQuestionScreenPreview() {
    WeQuizTheme {
        OwnerQuestionScreen(
            quiz = null,
            currentUserId = null,
            onQuizFinished = { _, _ -> },
        )
    }
}
