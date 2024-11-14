package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLeftChatBubble
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.CreateChoiceItems
import kr.boostcamp_2024.course.quiz.component.CreateQuestionContent
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuestionUiState
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuestionViewModel

@Composable
fun CreateQuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionSuccess: () -> Unit,
    viewModel: CreateQuestionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.createQuestionUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState) {
        if (uiState.creationSuccess) {
            onCreateQuestionSuccess()
        }
        uiState.snackBarMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            viewModel.setNewSnackBarMessage(null)
        }
    }

    CreateQuestionScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        focusRequester = focusRequester,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onSolutionChanged = viewModel::onSolutionChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onChoiceTextChanged = viewModel::onChoiceTextChanged,
        onSelectedChoiceNumChanged = viewModel::onSelectedChoiceNumChanged,
        onCreateQuestionButtonClick = viewModel::createQuestion,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionScreen(
    uiState: CreateQuestionUiState,
    focusRequester: FocusRequester,
    snackBarHostState: SnackbarHostState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSolutionChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onChoiceTextChanged: (Int, String) -> Unit,
    onSelectedChoiceNumChanged: (Int) -> Unit,
    onCreateQuestionButtonClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.top_app_bar_create_question),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigationButtonClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.des_btn_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    CreateQuestionGuideContent(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                item {
                    CreateQuestionContent(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        focusRequester = focusRequester,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        title = uiState.questionCreationInfo.title,
                        description = uiState.questionCreationInfo.description,
                        solution = uiState.questionCreationInfo.solution,
                        onTitleChanged = onTitleChanged,
                        onDescriptionChanged = onDescriptionChanged,
                        onSolutionChanged = onSolutionChanged,
                    )
                }

                item {
                    CreateChoiceItems(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        focusManager = focusManager,
                        choices = uiState.questionCreationInfo.choices,
                        selectedChoiceNum = uiState.questionCreationInfo.answer,
                        updateChoiceText = onChoiceTextChanged,
                        updateSelectedChoiceNum = onSelectedChoiceNumChanged,
                    )
                }

                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        enabled = uiState.isCreateQuestionValid,
                        onClick = onCreateQuestionButtonClick,
                    ) {
                        Text(
                            text = stringResource(id = R.string.btn_create_question),
                        )
                    }
                }
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
fun CreateQuestionGuideContent(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        WeQuizLocalRoundedImage(
            modifier = Modifier.size(120.dp),
            imagePainter = painterResource(id = R.drawable.img_clock_character),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            WeQuizLeftChatBubble(
                text = stringResource(id = R.string.txt_create_question_guide1),
            )
            WeQuizLeftChatBubble(
                text = stringResource(id = R.string.txt_create_question_guide2),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateQuestionScreenPreview() {
    WeQuizTheme {
        CreateQuestionScreen(
            onNavigationButtonClick = {},
            onCreateQuestionSuccess = {},
        )
    }
}
