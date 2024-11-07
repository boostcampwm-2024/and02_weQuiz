package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.CreateQuestionViewModel
import kr.boostcamp_2024.course.quiz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionSuccess: () -> Unit,
    viewModel: CreateQuestionViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(id = R.string.top_app_bar_create_question)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigationButtonClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.des_btn_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            CreateQuestionGuideContent(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CreateQuestionContent(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CreateChoiceItems(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    // todo: 문제 출제 처리
                    onCreateQuestionSuccess()
                },
            ) {
                Text(
                    text = stringResource(id = R.string.btn_create_question)
                )
            }


        }
    }
}

@Composable
fun CreateQuestionGuideContent(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ChatBubble(
                text = stringResource(id = R.string.txt_create_question_guide1)
            )
            ChatBubble(
                text = stringResource(id = R.string.txt_create_question_guide2)
            )
        }
    }
}

@Composable
fun CreateQuestionContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        WeQuizTextField(
            label = stringResource(id = R.string.txt_question_title_label),
            placeholder = stringResource(id = R.string.txt_question_title_placeholder)
        )
        WeQuizTextField(
            label = stringResource(id = R.string.txt_question_content_label),
            placeholder = stringResource(id = R.string.txt_question_content_placeholder),
            minLine = 6
        )
        WeQuizTextField(
            label = stringResource(id = R.string.txt_question_description_label),
            placeholder = stringResource(id = R.string.txt_question_description_placeholder),
            minLine = 6
        )
    }
}

@Composable
fun WeQuizTextField(
    label: String,
    placeholder: String,
    minLine: Int = 1
) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        minLines = minLine,
        trailingIcon = {
            IconButton(
                onClick = { text = "" }
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_cancel_on_surface_variant),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(id = R.string.des_clear_text)
                )
            }
        }
    )
}

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 20.dp
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CreateChoiceItems(
    modifier: Modifier = Modifier
) {
    var choice1Text by remember { mutableStateOf("") }
    var choice2Text by remember { mutableStateOf("") }
    var choice3Text by remember { mutableStateOf("") }
    var choice4Text by remember { mutableStateOf("") }
    var selectedChoiceNum by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (i in 1..4) {
            ChoiceItem(
                text = when (i) {
                    1 -> choice1Text
                    2 -> choice2Text
                    3 -> choice3Text
                    else -> choice4Text
                },
                onTextChanged = {
                    when (i) {
                        1 -> choice1Text = it
                        2 -> choice2Text = it
                        3 -> choice3Text = it
                        else -> choice4Text = it
                    }
                },
                isSelected = selectedChoiceNum == i,
                onSelected = { isSelected ->
                    if (isSelected) {
                        selectedChoiceNum = i
                    }
                }
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
    onSelected: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onSelected(!isSelected)
            }
        )
        OutlinedTextField(
            value = text,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.bodyLarge,
            trailingIcon = {
                IconButton(
                    onClick = { onTextChanged("") }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_cancel_on_surface_variant),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = stringResource(id = R.string.des_clear_text)
                    )
                }
            },
            placeholder = { Text(stringResource(id = R.string.txt_question_choice_placeholder)) }
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