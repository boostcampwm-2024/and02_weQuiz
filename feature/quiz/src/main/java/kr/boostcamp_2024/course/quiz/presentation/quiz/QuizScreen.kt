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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.QuizTopAppBar
import kr.boostcamp_2024.course.quiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: (String) -> Unit,
    onSettingMenuClick: (String, String) -> Unit,
    onQuizDeleted: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.value.isDeleted) {
        LaunchedEffect(Unit) {
            onQuizDeleted() // 삭제되면 뒤로가기
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initViewModel()
    }

    QuizScreen(
        category = uiState.value.category,
        quiz = uiState.value.quiz,
        snackbarHostState = snackbarHostState,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateQuestionButtonClick = onCreateQuestionButtonClick,
        onStartQuizButtonClick = onStartQuizButtonClick,
        onSettingMenuClick = onSettingMenuClick,
        onDeleteMenuClick = viewModel::deleteQuiz,
    )

    if (uiState.value.isLoading) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
            )
        }
    }

    uiState.value.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.shownErrorMessage()
        }
    }
}

@Composable
fun QuizScreen(
    category: Category?,
    quiz: BaseQuiz?,
    snackbarHostState: SnackbarHostState,
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: (String) -> Unit,
    onSettingMenuClick: (String, String) -> Unit,
    onDeleteMenuClick: (String, BaseQuiz) -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            QuizTopAppBar(onNavigationButtonClick, category, quiz, onSettingMenuClick, onDeleteMenuClick)
            // 300줄 넘어서 분리
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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

            WeQuizAsyncImage(
                modifier = Modifier.fillMaxSize(),
                imgUrl = quiz?.quizImageUrl,
                contentDescription = null,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                quiz?.let { quiz ->
                    // QuizTitle
                    Text(
                        text = quiz.title,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    // QuizDescription
                    quiz.description?.let { description ->
                        Text(
                            text = description,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }

                // QuizChip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {

                    quiz?.let {
                        ElevatedAssistChip(
                            onClick = { /* no-op */ },
                            label = {
                                Text(text = stringResource(R.string.txt_quiz_question_count, quiz.questions.size))
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

                    category?.let {
                        ElevatedAssistChip(
                            onClick = { /* no-op */ },
                            label = {
                                Text(
                                    text = category.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
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
                }
                Button(
                    onClick = {
                        if (quiz != null) {
                            onStartQuizButtonClick("5DQ3FCh26uLdTLFbLICz")
                        }
                    },
                ) {
                    Text(text = "123")
                }

                if (quiz is Quiz) {
                    // CreateQuestionButton & StartQuizButton
                    quiz.let {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onCreateQuestionButtonClick(quiz.id) },
                            enabled = quiz.isOpened.not(),
                        ) {
                            when (quiz.isOpened.not()) {
                                true -> Text(text = stringResource(R.string.txt_open_create_question))
                                false -> Text(text = stringResource(R.string.txt_close_create_question))
                            }

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onStartQuizButtonClick(quiz.id) },
                                enabled = (quiz.isOpened && quiz.questions.isNotEmpty()),
                            ) {
                                when (quiz.isOpened && quiz.questions.isEmpty()) {
                                    true -> Text(text = stringResource(R.string.txt_quiz_question_count_zero))
                                    false -> Text(text = stringResource(R.string.txt_quiz_start))
                                }
                            }
                        }
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
            category = Category(
                id = "id",
                name = "name",
                description = "description",
                categoryImageUrl = "categoryImageUrl",
                quizzes = emptyList(),
            ),
            quiz = Quiz(
                id = "id",
                title = "퀴즈 제목임",
                description = "퀴즈 설명임",
                startTime = "startTime",
                solveTime = 60,
                questions = emptyList(),
                userOmrs = emptyList(),
                quizImageUrl = "quizImageUrl",
            ),
            snackbarHostState = SnackbarHostState(),
            onNavigationButtonClick = {},
            onCreateQuestionButtonClick = {},
            onStartQuizButtonClick = {},
            onSettingMenuClick = { _, _ -> },
            onDeleteMenuClick = { _, _ -> },
        )
    }
}
