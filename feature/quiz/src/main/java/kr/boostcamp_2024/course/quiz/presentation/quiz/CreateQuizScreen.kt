package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.ChatBubbleLeft
import kr.boostcamp_2024.course.quiz.component.CircleImage
import kr.boostcamp_2024.course.quiz.component.QuizDatePickerTextField
import kr.boostcamp_2024.course.quiz.component.QuizDescriptionTextField
import kr.boostcamp_2024.course.quiz.component.QuizSolveTimeSlider
import kr.boostcamp_2024.course.quiz.component.QuizTitleTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuizSuccess: () -> Unit,        // TODO: 퀴즈 생성
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
                            contentDescription = null
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
                CircleImage(
                    modifier = Modifier.size(120.dp),
                    imagePainter = painterResource(R.drawable.sample_profile),
                    contentDescription = null
                )
                ChatBubbleLeft(text = stringResource(R.string.txt_create_quiz_guide))
            }

            // QuizInfo
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Title
                var quizTitle by remember { mutableStateOf("") }
                QuizTitleTextField(
                    quizTitle = quizTitle,
                    onValueChange = { quizTitle = it },
                    onClearClick = { quizTitle = "" }
                )

                //Description
                var quizDescription by remember { mutableStateOf("") }
                QuizDescriptionTextField(
                    quizDescription = quizDescription,
                    onValueChange = { quizDescription = it },
                    onClearClick = { quizDescription = "" }
                )

                // StartTime
                Text(text = stringResource(R.string.txt_quiz_start_time))
                var quizDate by remember { mutableStateOf("") }
                QuizDatePickerTextField(
                    onDateSelected = { quizDate = it }
                )

                // SolveTime
                Text(text = stringResource(R.string.txt_quiz_solve_time))
                var quizSolveTime by remember { mutableFloatStateOf(0f) }

                QuizSolveTimeSlider(
                    value = quizSolveTime,
                    steps = 8,
                    valueRange = 10f..100f,
                    onValueChange = { quizSolveTime = it }
                )

                // CreateButton
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateQuizSuccess    // TODO: 퀴즈 생성
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
    CreateQuizScreen(
        onNavigationButtonClick = {},
        onCreateQuizSuccess = {},
    )
}