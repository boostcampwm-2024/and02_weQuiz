package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
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
import kr.boostcamp_2024.course.quiz.component.ConsumeBlankContentUi
import kr.boostcamp_2024.course.quiz.component.ConsumeTextContentUi
import kr.boostcamp_2024.course.quiz.component.CreateChoiceItems
import kr.boostcamp_2024.course.quiz.component.CreateQuestionContent
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuestionUiState
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuestionViewModel
import kr.boostcamp_2024.course.quiz.viewmodel.BlankQuestionItem

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
        stringResource(R.string.txt_create_blank_question),
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
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { },
                    selected = index == 1,
                ) {
                    Text(
                        text = label,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
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
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 10.dp,
                            ),
                            text = "낱말 맞추기 문제 만들기",
                        )
                        FlowRow(
                            modifier = Modifier
                                .fillMaxSize(1f)
                                .padding(16.dp)
                                .background(Color.Gray)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            blankQuestionItems.forEachIndexed { index, it ->
                                when (it) {
                                    is BlankQuestionItem.Blank -> {
                                        ConsumeBlankContentUi(
                                            word = it.text,
                                            index = index,
                                            onContentRemove = onContentRemove,
                                            onValueChanged = onBlankQuestionItemValueChanged,
                                        )
                                    }

                                    is BlankQuestionItem.Text -> {
                                        ConsumeTextContentUi(
                                            word = it.text,
                                            index = index,
                                            onContentRemove = onContentRemove,
                                            onValueChanged = onBlankQuestionItemValueChanged,

                                            )
                                    }

                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        ) {
                            Button(
                                onClick = onAddTextItemButtonClick,
                                enabled = isCreateTextButtonValid,
                            ) {
                                Text(stringResource(R.string.btn_create_text))
                            }

                            Button(
                                onClick = onAddBlankItemButtonClick,
                                enabled = isCreateBlankButtonValid,
                            ) {
                                Text(stringResource(R.string.btn_create_blank))
                            }
                        }
                    }
                }
                if (!isBlankQuestion) {
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
                            enabled = uiState.isCreateBlankQuestionValid,
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
