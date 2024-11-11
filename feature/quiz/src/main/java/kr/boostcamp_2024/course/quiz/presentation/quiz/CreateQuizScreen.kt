package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLeftChatBubble
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.QuizDatePickerTextField
import kr.boostcamp_2024.course.quiz.component.QuizDescriptionTextField
import kr.boostcamp_2024.course.quiz.component.QuizSolveTimeSlider
import kr.boostcamp_2024.course.quiz.component.QuizTitleTextField
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuizViewModel

@Composable
fun CreateQuizScreen(
    viewModel: CreateQuizViewModel = hiltViewModel(),
    onNavigationButtonClick: () -> Unit,
    onCreateQuizSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateQuizScreen(
        quizTitle = uiState.quizTitle,
        quizDescription = uiState.quizDescription,
        quizSolveTime = uiState.quizSolveTime,
        onQuizTitleChange = viewModel::setQuizTitle,
        onQuizDescriptionChange = viewModel::setQuizDescription,
        onQuizDateChange = viewModel::setQuizDate,
        onQuizSolveTimeChange = viewModel::setQuizSolveTime,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateQuizButtonClick = viewModel::createQuiz
    )

    if (uiState.isCreateQuizSuccess) {
        LaunchedEffect(Unit) {
            onCreateQuizSuccess()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    quizTitle: String,
    quizDescription: String,
    quizSolveTime: Float,
    onQuizTitleChange: (String) -> Unit,
    onQuizDescriptionChange: (String) -> Unit,
    onQuizDateChange: (String) -> Unit,
    onQuizSolveTimeChange: (Float) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onCreateQuizButtonClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.top_app_bar_create_quiz))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.btn_navigation)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Character Guide

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WeQuizLocalRoundedImage(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    imagePainter = painterResource(R.drawable.sample_profile),
                    contentDescription = null
                )
                WeQuizLeftChatBubble(
                    text = stringResource(R.string.txt_create_quiz_guide)
                )
            }

            // QuizInfo
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Title
                QuizTitleTextField(
                    quizTitle = quizTitle,
                    onValueChange = { onQuizTitleChange(it) }
                )

                //Description
                QuizDescriptionTextField(
                    quizDescription = quizDescription,
                    onValueChange = { onQuizDescriptionChange(it) }
                )

                // StartTime
                Text(text = stringResource(R.string.txt_quiz_start_time))
                QuizDatePickerTextField(
                    onDateSelected = { onQuizDateChange(it) }
                )

                // SolveTime
                Text(text = stringResource(R.string.txt_quiz_solve_time))
                QuizSolveTimeSlider(
                    value = quizSolveTime,
                    steps = 8,
                    valueRange = 10f..100f,
                    onValueChange = { onQuizSolveTimeChange(it) }
                )

                // CreateButton
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateQuizButtonClick
                ) {
                    Text(text = stringResource(R.string.btn_create_quiz))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateQuizScreenPreview() {
    WeQuizTheme {
        CreateQuizScreen(
            quizTitle = "",
            quizDescription = "",
            quizSolveTime = 0f,
            onQuizTitleChange = {},
            onQuizDescriptionChange = {},
            onQuizDateChange = {},
            onQuizSolveTimeChange = {},
            onNavigationButtonClick = {},
            onCreateQuizButtonClick = {}
        )
    }
}