package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.component.QuizContent
import kr.boostcamp_2024.course.quiz.utils.timerFormat

@Composable
internal fun GeneralQuestionScreen(
    quizTitle: String?,
    currentPage: Int,
    questions: List<Question>,
    countDownTime: Int,
    selectedIndexList: List<Any>,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigationButtonClick: () -> Unit,
    onOptionSelected: (Int, Int) -> Unit,
    onBlanksSelected: (Int, Map<String, String?>) -> Unit,
    onNextButtonClick: () -> Unit,
    onPreviousButtonClick: () -> Unit,
    onSubmitButtonClick: () -> Unit,
    showErrorMessage: (Int) -> Unit,
    blankQuestionContents: List<Map<String, Any>?>,
    blankWords: List<Map<String, Any>>,
    removeBlankContent: (Int) -> Unit,
    addBlankContent: (Int) -> Unit,
    getBlankQuestionAnswer: () -> Map<String, String?>,
    isLoading: Boolean,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var buttonsHeight by remember { mutableStateOf(IntSize.Zero) }

    BackHandler {
        showDialog = showDialog.not()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            quizTitle?.let {
                QuestionTopBar(
                    title = it,
                    onShowDialog = { showDialog = true },
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        LinearProgressIndicator(
            progress = { (currentPage + 1) / questions.size.toFloat() },
            modifier = Modifier.fillMaxWidth(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        ) {
            LazyColumn {
                item {
                    GeneralQuizGuide(countDownTime = countDownTime)
                }

                item {
                    QuizContent(
                        currentPage = currentPage,
                        selectedIndexList = selectedIndexList,
                        onOptionSelected = onOptionSelected,
                        questions = questions,
                        showErrorMessage = showErrorMessage,
                        onBlanksSelected = onBlanksSelected,
                        blankQuestionContents = blankQuestionContents,
                        blankWords = blankWords,
                        removeBlankContent = removeBlankContent,
                        addBlankContent = addBlankContent,
                        getBlankQuestionAnswer = getBlankQuestionAnswer,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(with(LocalDensity.current) { buttonsHeight.height.toDp() }))
                }
            }

            GeneralQuizButtons(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .padding(10.dp),
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
                prevButtonEnabled = currentPage > 0,
                setButtonsHeight = { buttonsHeight = it },
                isNextButtonEnabled = !isLoading,
            )
        }

        if (showDialog) {
            GeneralQuizDialog(
                currentPage = currentPage,
                questions = questions,
                closeDialog = { showDialog = false },
                onNavigationButtonClick = onNavigationButtonClick,
                onSubmitButtonClick = onSubmitButtonClick,
            )
        }
    }
}

@Composable
fun GeneralQuizGuide(
    countDownTime: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
    ) {
        WeQuizRightChatBubble(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "${stringResource(R.string.txt_question_timer)} ${timerFormat(countDownTime)}",
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

@Composable
fun GeneralQuizButtons(
    modifier: Modifier = Modifier,
    setButtonsHeight: (IntSize) -> Unit,
    nextButtonText: String,
    onNextButtonClick: () -> Unit,
    onPrevButtonClick: () -> Unit,
    prevButtonEnabled: Boolean,
    isNextButtonEnabled: Boolean,
) {
    Column(
        modifier = modifier.onGloballyPositioned { setButtonsHeight(it.size) },
    ) {
        Button(
            enabled = isNextButtonEnabled,
            onClick = onNextButtonClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = nextButtonText)
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

@Composable
fun GeneralQuizDialog(
    currentPage: Int,
    questions: List<Question>,
    closeDialog: () -> Unit,
    onNavigationButtonClick: () -> Unit,
    onSubmitButtonClick: () -> Unit,
) {
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
            closeDialog()
            if (currentPage == questions.size - 1) {
                onSubmitButtonClick()
            } else {
                onNavigationButtonClick()
            }
        },
        onDismissRequest = closeDialog,
        dialogImage = painterResource(id = R.drawable.quiz_system_profile),
        content = { /* no-op */ },
    )
}

@Preview(locale = "ko")
@PreviewLightDark
@Composable
fun GeneralQuestionScreenPreview() {
    WeQuizTheme {
        GeneralQuestionScreen(
            quizTitle = "Quiz Title",
            currentPage = 0,
            questions = listOf(),
            countDownTime = 0,
            selectedIndexList = emptyList(),
            snackbarHostState = SnackbarHostState(),
            onNavigationButtonClick = {},
            onOptionSelected = { _, _ -> },
            onNextButtonClick = {},
            onPreviousButtonClick = {},
            onSubmitButtonClick = {},
            showErrorMessage = {},
            onBlanksSelected = { _, _ -> },
            blankQuestionContents = emptyList(),
            blankWords = emptyList(),
            removeBlankContent = {},
            addBlankContent = {},
            getBlankQuestionAnswer = { emptyMap() },
            isLoading = false,
        )
    }
}
