package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: () -> Unit,
) {
    // 매개변수에 hiltViewModel()
    // 뷰모델에서 받아서 아래에 넣어주기

    QuizScreen(
        quizTitle = "퀴즈 제목 들어감",
        quizDescription = "퀴즈 내용 쏼라 쏼라 퀴즈 내용 쏼라 쏼라 퀴즈 내용 쏼라 쏼라 퀴즈 내용 쏼라 쏼라",
        quizCategory = "카테고리 들어감",
        quizQuestionCount = 10,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateQuestionButtonClick = onCreateQuestionButtonClick,
        onStartQuizButtonClick = onStartQuizButtonClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    quizTitle: String,
    quizDescription: String,
    quizCategory: String,
    quizQuestionCount: Int,
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: () -> Unit,
) {
    // TODO: startTime으로 계산
    // 일단 임시로 여기에
    var canCreateQuestion by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigationButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        bottom = innerPadding.calculateBottomPadding(),
                    ),
                ),
        ) {
            // TODO: 이미지
            WeQuizAsyncImage(
                modifier = Modifier.fillMaxSize(),
                imgUrl = "",
                contentDescription = null,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // QuizTitle
                Text(
                    text = quizTitle,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // QuizDescription
                Text(
                    text = quizDescription,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                // QuizChip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ElevatedAssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = stringResource(
                                    R.string.txt_quiz_question_count,
                                    quizQuestionCount,
                                ),
                            )
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(R.drawable.edit_24),
                                contentDescription = null,
                            )
                        },
                    )
                    ElevatedAssistChip(
                        onClick = { },
                        label = {
                            Text(text = quizCategory)
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(R.drawable.search_24),
                                contentDescription = null,
                            )
                        },
                    )
                }

                // CreateQuestionButton & StartQuizButton
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onCreateQuestionButtonClick("2k1QrCuOUHLERgQAmMqg") },
                    enabled = canCreateQuestion,
                ) {
                    when (canCreateQuestion) {
                        true -> Text(text = stringResource(R.string.txt_open_create_question))
                        false -> Text(text = stringResource(R.string.txt_close_create_question))
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onStartQuizButtonClick() },
                    enabled = (canCreateQuestion || quizQuestionCount == 0).not(),
                ) {
                    when (canCreateQuestion.not() && quizQuestionCount == 0) {
                        true -> Text(text = stringResource(R.string.txt_quiz_question_count_zero))
                        false -> Text(text = stringResource(R.string.txt_quiz_start))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizStartScreenPreview() {
    WeQuizTheme {
        QuizScreen(
            quizTitle = "퀴즈 제목 들어감",
            quizDescription = "퀴즈 내용 쏼라 쏼라 퀴즈 내용 쏼라 쏼라 퀴즈 내용 쏼라 쏼라 퀴즈 내용 쏼라 쏼라 퀴즈 내용 쏼라 쏼라 퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라퀴즈 내용 쏼라 쏼라",
            quizCategory = "카테고리 들어감",
            quizQuestionCount = 10,
            onNavigationButtonClick = {},
            onCreateQuestionButtonClick = {},
            onStartQuizButtonClick = {},
        )
    }
}
