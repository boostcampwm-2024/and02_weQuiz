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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.CreateBlankQuestionContent
import kr.boostcamp_2024.course.quiz.component.CreateChoiceItems
import kr.boostcamp_2024.course.quiz.component.CreateQuestionContent
import kr.boostcamp_2024.course.quiz.component.QuizAiDialog
import kr.boostcamp_2024.course.quiz.viewmodel.BlankQuestionItem
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
    val options = listOf(
        stringResource(R.string.txt_create_general_question),
        stringResource(R.string.txt_blank_question),
    )
    LaunchedEffect(uiState) {
        if (uiState.creationSuccess) {
            onCreateQuestionSuccess()
        }
        uiState.snackBarMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            viewModel.setNewSnackBarMessage(null)
        }
    }
    if (uiState.showDialog) {
        QuizAiDialog(
            onDismissButtonClick = { viewModel.closeDialog() },
            onConfirmButtonClick = { category ->
                viewModel.getAiRecommendedQuestion(category)
                viewModel.closeDialog()
            },
        )
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
        options = options,
        onQuestionTypeIndexChange = viewModel::onQuestionTypeIndexChange,
        selectedQuestionTypeIndex = uiState.selectedQuestionTypeIndex,
        isBlankQuestion = uiState.isBlankQuestion,
        blankQuestionItems = uiState.items,
        onAddBlankItemButtonClick = viewModel::addBlankItem,
        onAddTextItemButtonClick = viewModel::addTextItem,
        onBlankQuestionItemValueChanged = viewModel::onBlankQuestionItemValueChanged,
        onContentRemove = viewModel::onContentRemove,
        onCreateBlankQuestionButtonClick = viewModel::onCreateBlankQuestion,
        isCreateBlankButtonValid = uiState.isCreateBlankButtonValid,
        isCreateTextButtonValid = uiState.isCreateTextButtonValid,
        onShowDialog = viewModel::showDialog,
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
    onQuestionTypeIndexChange: (Int) -> Unit,
    selectedQuestionTypeIndex: Int,
    options: List<String>,
    isBlankQuestion: Boolean,
    blankQuestionItems: List<BlankQuestionItem>,
    onAddBlankItemButtonClick: () -> Unit,
    onAddTextItemButtonClick: () -> Unit,
    onBlankQuestionItemValueChanged: (String, Int) -> Unit,
    onContentRemove: (Int) -> Unit,
    onCreateBlankQuestionButtonClick: () -> Unit,
    isCreateBlankButtonValid: Boolean,
    isCreateTextButtonValid: Boolean,
    onShowDialog: () -> Unit,
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
                                selected = index == selectedQuestionTypeIndex,
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
                        isBlankQuestion = isBlankQuestion,
                    )
                }

                if (!isBlankQuestion) {
                    item {
                        CreateChoiceItems(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            focusManager = focusManager,
                            choices = uiState.choiceQuestionCreationInfo.choices,
                            selectedChoiceNum = uiState.choiceQuestionCreationInfo.answer,
                            updateChoiceText = onChoiceTextChanged,
                            updateSelectedChoiceNum = onSelectedChoiceNumChanged,
                        )
                    }
                }
                if (isBlankQuestion) {
                    item {
                        CreateBlankQuestionContent(
                            blankQuestionItems,
                            onContentRemove,
                            onBlankQuestionItemValueChanged,
                            onAddTextItemButtonClick,
                            isCreateTextButtonValid,
                            onAddBlankItemButtonClick,
                            isCreateBlankButtonValid,
                        )
                    }
                }
                if (!isBlankQuestion) {
                    item {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            enabled = uiState.isCreateQuestionValid && !uiState.isLoading,
                            onClick = onCreateQuestionButtonClick,
                        ) {
                            Text(
                                text = stringResource(id = R.string.btn_create_question),
                            )
                        }
                    }
                } else {
                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 10.dp,
                            ),
                        )

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            enabled = uiState.isCreateBlankQuestionValid && !uiState.isLoading,
                            onClick = onCreateBlankQuestionButtonClick,
                        ) {
                            Text(
                                text = stringResource(id = R.string.btn_create_question),
                            )
                        }
                    }
                }
            }
            if (uiState.isLoading) {
                AiLoadingIndicator()
            }
            if (!isBlankQuestion) {
                FloatingActionButton(
                    onClick = { onShowDialog() },
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
