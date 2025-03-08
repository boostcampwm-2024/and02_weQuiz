package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.CreateBlankQuestionContent
import kr.boostcamp_2024.course.quiz.component.CreateChoiceItems
import kr.boostcamp_2024.course.quiz.component.CreateQuestionContent
import kr.boostcamp_2024.course.quiz.component.QuizAiDialog
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuestionUiState
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuestionViewModel

@Composable
internal fun CreateQuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionSuccess: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowErrorSnackbar: (Throwable) -> Unit,
    viewModel: CreateQuestionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.createQuestionUiState.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collectLatest { throwable -> onShowErrorSnackbar(throwable) }
    }

    LaunchedEffect(uiState.creationSuccess) {
        if (uiState.creationSuccess) {
            onCreateQuestionSuccess()
        }
    }

    CreateQuestionScreen(
        uiState = uiState,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onSolutionChanged = viewModel::onSolutionChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onChoiceTextChanged = viewModel::onChoiceTextChanged,
        onSelectedChoiceNumChanged = viewModel::onSelectedChoiceNumChanged,
        onCreateQuestionButtonClick = viewModel::createQuestion,
        onQuestionTypeIndexChange = viewModel::onQuestionTypeIndexChange,
        onAddBlankItemButtonClick = viewModel::addBlankItem,
        onAddTextItemButtonClick = viewModel::addTextItem,
        onBlankQuestionItemValueChanged = viewModel::onBlankQuestionItemValueChanged,
        onContentRemove = viewModel::onContentRemove,
        onCreateBlankQuestionButtonClick = viewModel::onCreateBlankQuestion,
        onShowDialog = viewModel::showDialog,
        focusRequester = focusRequester,
        snackbarHostState = snackbarHostState,
    )

    if (uiState.showDialog) {
        QuizAiDialog(
            onDismissButtonClick = { viewModel.closeDialog() },
            onConfirmButtonClick = { category ->
                viewModel.getAiRecommendedQuestion(category)
                viewModel.closeDialog()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateQuestionScreen(
    uiState: CreateQuestionUiState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSolutionChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onChoiceTextChanged: (Int, String) -> Unit,
    onSelectedChoiceNumChanged: (Int) -> Unit,
    onCreateQuestionButtonClick: () -> Unit,
    onQuestionTypeIndexChange: (Int) -> Unit,
    onAddBlankItemButtonClick: () -> Unit,
    onAddTextItemButtonClick: () -> Unit,
    onBlankQuestionItemValueChanged: (String, Int) -> Unit,
    onContentRemove: (Int) -> Unit,
    onCreateBlankQuestionButtonClick: () -> Unit,
    onShowDialog: () -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val focusManager = LocalFocusManager.current
    val options = listOf(
        stringResource(R.string.txt_create_general_question),
        stringResource(R.string.txt_blank_question),
    )

    Scaffold(
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        val imeInsets = WindowInsets.ime
        val density = LocalDensity.current
        val keyboardHeight = with(density) { imeInsets.getBottom(density).toDp() }
        val isKeyboardVisible = keyboardHeight > 0.dp
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = if (isKeyboardVisible) keyboardHeight else innerPadding.calculateBottomPadding(),
                ),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 65.dp, vertical = 10.dp),
                    ) {
                        options.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                                onClick = {
                                    onQuestionTypeIndexChange(index)
                                },
                                selected = index == uiState.selectedQuestionTypeIndex,
                            ) {
                                Text(
                                    text = label,
                                )
                            }
                        }
                    }
                }

                item {
                    CreateQuestionContent(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        focusRequester = focusRequester,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        title = uiState.choiceQuestionCreationInfo.title,
                        description = uiState.choiceQuestionCreationInfo.description,
                        solution = uiState.choiceQuestionCreationInfo.solution,
                        onTitleChanged = onTitleChanged,
                        onDescriptionChanged = onDescriptionChanged,
                        onSolutionChanged = onSolutionChanged,
                        isBlankQuestion = uiState.isBlankQuestion,
                    )
                }

                item {
                    if (!uiState.isBlankQuestion) {
                        CreateChoiceItems(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            focusManager = focusManager,
                            choices = uiState.choiceQuestionCreationInfo.choices,
                            selectedChoiceNum = uiState.choiceQuestionCreationInfo.answer,
                            updateChoiceText = onChoiceTextChanged,
                            updateSelectedChoiceNum = onSelectedChoiceNumChanged,
                        )
                    } else {
                        CreateBlankQuestionContent(
                            uiState.items,
                            onContentRemove,
                            onBlankQuestionItemValueChanged,
                            onAddTextItemButtonClick,
                            uiState.isCreateTextButtonValid,
                            onAddBlankItemButtonClick,
                            uiState.isCreateBlankButtonValid,
                        )
                    }
                }

                item {
                    if (uiState.isBlankQuestion) {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 10.dp,
                            ),
                        )
                    }

                    val createButtonEnabled = if (uiState.isBlankQuestion) {
                        uiState.isCreateBlankQuestionValid && !uiState.isLoading
                    } else {
                        uiState.isCreateQuestionValid && !uiState.isLoading
                    }
                    val onCreateButtonClick = if (uiState.isBlankQuestion) {
                        onCreateBlankQuestionButtonClick
                    } else {
                        onCreateQuestionButtonClick
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        enabled = createButtonEnabled,
                        onClick = onCreateButtonClick,
                    ) {
                        Text(
                            text = stringResource(id = R.string.btn_create_question),
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                WeQuizCircularProgressIndicator()
            }
            if (uiState.isAiLoading) {
                AiLoadingIndicator()
            }
            if (!uiState.isBlankQuestion) {
                FloatingActionButton(
                    onClick = { if (!uiState.isAiLoading) onShowDialog() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(80.dp)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.image_ai),
                        contentDescription = stringResource(R.string.btn_create_quiz_ai),
                    )
                }
            }
        }
    }
}

@PreviewKoLightDark
@Composable
private fun CreateQuestionScreenPreview() {
    val previewCreateQuestionUiState = CreateQuestionUiState()

    WeQuizTheme {
        CreateQuestionScreen(
            uiState = previewCreateQuestionUiState,
            onTitleChanged = {},
            onDescriptionChanged = {},
            onSolutionChanged = {},
            onNavigationButtonClick = {},
            onChoiceTextChanged = { _, _ -> },
            onSelectedChoiceNumChanged = {},
            onCreateQuestionButtonClick = {},
            onQuestionTypeIndexChange = {},
            onAddBlankItemButtonClick = {},
            onAddTextItemButtonClick = {},
            onBlankQuestionItemValueChanged = { _, _ -> },
            onContentRemove = {},
            onCreateBlankQuestionButtonClick = {},
            onShowDialog = {},
        )
    }
}
