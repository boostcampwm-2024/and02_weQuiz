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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.component.QuizContent
import kr.boostcamp_2024.course.quiz.component.QuizOwnerDialog
import kr.boostcamp_2024.course.quiz.component.RealTimeQuizGuideContent
import kr.boostcamp_2024.course.quiz.viewmodel.OwnerQuestionViewModel

@Composable
internal fun OwnerQuestionScreen(
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
        isNextButtonEnabled = !uiState.isQuizFinished,
        currentPage = uiState.currentPage,
        choiceQuestions = uiState.questions,
        ownerName = uiState.ownerName ?: "",
        snackbarHostState = snackbarHostState,
        onNextButtonClick = questionViewModel::nextPage,
        onPreviousButtonClick = questionViewModel::previousPage,
        onQuizFinishButtonClick = questionViewModel::setQuizFinished,
        showErrorMessage = questionViewModel::showErrorMessage,
        blankQuestionContents = uiState.blankQuestionContents,
        blankWords = uiState.blankWords,
        removeBlankWord = questionViewModel.blankQuestionManager::removeBlankContent,
        addBlankWord = questionViewModel.blankQuestionManager::addBlankContent,
        getBlankQuestionAnswer = questionViewModel.blankQuestionManager::getAnswer,
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
    isNextButtonEnabled: Boolean,
    currentPage: Int,
    choiceQuestions: List<Question?>,
    ownerName: String,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNextButtonClick: () -> Unit,
    onPreviousButtonClick: () -> Unit,
    onQuizFinishButtonClick: () -> Unit,
    showErrorMessage: (Int) -> Unit,
    blankQuestionContents: List<Map<String, Any>?>,
    blankWords: List<Map<String, Any>>,
    removeBlankWord: (Int) -> Unit,
    addBlankWord: (Int) -> Unit,
    getBlankQuestionAnswer: () -> Map<String, String?>,
) {
    var showQuitQuizDialog by rememberSaveable { mutableStateOf(false) }
    var showFinishQuizDialog by rememberSaveable { mutableStateOf(false) }
    val currentQuestion = choiceQuestions.getOrNull(currentPage)
    var buttonsHeight by remember { mutableStateOf(IntSize.Zero) }

    BackHandler {
        showQuitQuizDialog = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            quiz?.let {
                QuestionTopBar(
                    title = it.title,
                    onShowDialog = { showQuitQuizDialog = true },
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
                progress = { (currentPage + 1) / choiceQuestions.size.toFloat() },
                modifier = Modifier.fillMaxWidth(),
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (quiz != null && currentQuestion != null) {
                    val submittedParticipants = if (currentQuestion is ChoiceQuestion) {
                        currentQuestion.userAnswers.sum()
                    } else {
                        currentQuestion.userAnswers.size
                    }
                    item {
                        RealTimeQuizGuideContent(
                            ownerName = ownerName,
                            totalParticipants = quiz.waitingUsers.size,
                            submittedParticipants = submittedParticipants,
                        )
                    }
                    item {
                        QuizContent(
                            isOwner = true,
                            isRealTime = true,
                            currentPage = currentPage,
                            selectedIndexList = choiceQuestions.map { it?.userAnswers },
                            onOptionSelected = { _, _ -> },
                            questions = choiceQuestions,
                            showErrorMessage = showErrorMessage,
                            onBlanksSelected = { _, _ -> },
                            blankQuestionContents = blankQuestionContents,
                            blankWords = blankWords,
                            removeBlankContent = removeBlankWord,
                            addBlankContent = addBlankWord,
                            getBlankQuestionAnswer = getBlankQuestionAnswer,
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(with(LocalDensity.current) { buttonsHeight.height.toDp() }))
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
                nextButtonText = if (currentPage == choiceQuestions.size - 1) {
                    stringResource(R.string.txt_question_done)
                } else {
                    stringResource(R.string.txt_question_next_question)
                },
                onNextButtonClick = {
                    if (currentPage < choiceQuestions.size - 1) {
                        onNextButtonClick()
                    } else {
                        showFinishQuizDialog = true
                    }
                },
                onPrevButtonClick = {
                    if (currentPage > 0) onPreviousButtonClick()
                },
                setButtonsHeight = { buttonsHeight = it },
                isNextButtonEnabled = isNextButtonEnabled,
            )
        }

        if (showQuitQuizDialog || showFinishQuizDialog) {
            QuizOwnerDialog(
                isQuit = showQuitQuizDialog,
                onDismissButtonClick = {
                    if (showFinishQuizDialog) {
                        showFinishQuizDialog = false
                    } else {
                        showQuitQuizDialog = false
                    }
                },
                onFinishQuizButtonClick = onQuizFinishButtonClick,
            )
        }
    }
}

@Composable
fun RealTimeQuizWithOwnerButtons(
    modifier: Modifier = Modifier,
    prevButtonEnabled: Boolean,
    nextButtonText: String,
    onNextButtonClick: () -> Unit,
    onPrevButtonClick: () -> Unit,
    setButtonsHeight: (IntSize) -> Unit,
    isNextButtonEnabled: Boolean,
) {
    Column(
        modifier = modifier.onGloballyPositioned { setButtonsHeight(it.size) },
    ) {
        Button(
            onClick = onNextButtonClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = isNextButtonEnabled,
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

@Preview(locale = "ko")
@PreviewLightDark
@Composable
fun OwnerQuestionScreenPreview(
    @PreviewParameter(QuizParameterProvider::class, 1) quiz: RealTimeQuiz,
) {
    WeQuizTheme {
        OwnerQuestionScreen(
            quiz = quiz,
            isNextButtonEnabled = true,
            currentPage = 0,
            choiceQuestions = emptyList(),
            ownerName = "Owner",
            onNextButtonClick = {},
            onPreviousButtonClick = {},
            onQuizFinishButtonClick = {},
            showErrorMessage = {},
            blankQuestionContents = emptyList(),
            blankWords = emptyList(),
            removeBlankWord = {},
            addBlankWord = {},
            getBlankQuestionAnswer = { emptyMap() },
        )
    }
}
