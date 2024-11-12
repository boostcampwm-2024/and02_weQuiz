package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.CreateQuestionUiState
import kr.boostcamp_2024.course.quiz.CreateQuestionViewModel
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.presentation.component.ChatBubble
import kr.boostcamp_2024.course.quiz.presentation.component.WeQuizTextField

@Composable
fun CreateQuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionSuccess: () -> Unit,
    viewModel: CreateQuestionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.createQuestionUiState.collectAsStateWithLifecycle()

    CreateQuestionScreen(
        uiState = uiState,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onSolutionChanged = viewModel::onSolutionChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onChoiceTextChanged = viewModel::onChoiceTextChanged,
        onSelectedChoiceNumChanged = viewModel::onSelectedChoiceNumChanged,
        onCreateQuestionSuccess = onCreateQuestionSuccess,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionScreen(
    uiState: CreateQuestionUiState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSolutionChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onChoiceTextChanged: (Int, String) -> Unit,
    onSelectedChoiceNumChanged: (Int) -> Unit,
    onCreateQuestionSuccess: () -> Unit,
) {
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
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            CreateQuestionGuideContent(
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            CreateQuestionContent(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = uiState.questionCreationInfo.title,
                description = uiState.questionCreationInfo.description,
                solution = uiState.questionCreationInfo.solution,
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged,
                onSolutionChanged = onSolutionChanged,
            )
            CreateChoiceItems(
                modifier = Modifier.padding(horizontal = 16.dp),
                choices = uiState.questionCreationInfo.choices,
                selectedChoiceNum = uiState.questionCreationInfo.answer,
                updateChoiceText = onChoiceTextChanged,
                updateSelectedChoiceNum = onSelectedChoiceNumChanged,
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = uiState.isCreateQuestionValid,
                onClick = {
                    // todo: 문제 출제 처리
                    onCreateQuestionSuccess()
                },
            ) {
                Text(
                    text = stringResource(id = R.string.btn_create_question),
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
        Image(
            painter = painterResource(id = R.drawable.img_clock_character),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            ChatBubble(
                text = stringResource(id = R.string.txt_create_question_guide1),
            )
            ChatBubble(
                text = stringResource(id = R.string.txt_create_question_guide2),
            )
        }
    }
}

@Composable
fun CreateQuestionContent(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    solution: String? = null,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSolutionChanged: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        WeQuizTextField(
            label = stringResource(id = R.string.txt_question_title_label),
            text = title,
            onTextChanged = onTitleChanged,
            placeholder = stringResource(id = R.string.txt_question_title_placeholder),
        )
        WeQuizTextField(
            label = stringResource(id = R.string.txt_question_content_label),
            text = description ?: "",
            onTextChanged = onDescriptionChanged,
            placeholder = stringResource(id = R.string.txt_question_content_placeholder),
            minLine = 6,
        )
        WeQuizTextField(
            label = stringResource(id = R.string.txt_question_description_label),
            text = solution ?: "",
            onTextChanged = onSolutionChanged,
            placeholder = stringResource(id = R.string.txt_question_description_placeholder),
            minLine = 6,
        )
    }
}

@Composable
fun CreateChoiceItems(
    modifier: Modifier = Modifier,
    choices: List<String>,
    selectedChoiceNum: Int,
    updateChoiceText: (Int, String) -> Unit,
    updateSelectedChoiceNum: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        choices.forEachIndexed { index, choiceText ->
            ChoiceItem(
                text = choiceText,
                onTextChanged = { updateChoiceText(index, it) },
                isSelected = selectedChoiceNum == index,
                onSelected = { updateSelectedChoiceNum(index) },
            )
        }
    }
}

@Composable
fun ChoiceItem(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onSelected(!isSelected)
            },
        )
        OutlinedTextField(
            value = text,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.bodyLarge,
            trailingIcon = {
                IconButton(
                    onClick = { onTextChanged("") },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_cancel_on_surface_variant),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = stringResource(id = R.string.des_clear_text),
                    )
                }
            },
            placeholder = { Text(stringResource(id = R.string.txt_question_choice_placeholder)) },
        )
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
