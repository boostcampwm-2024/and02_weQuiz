package kr.boostcamp_2024.course.quiz.presentation.quiz

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.QuestionResult
import kr.boostcamp_2024.course.domain.model.QuizResult
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.viewmodel.QuizResultViewModel

@Composable
fun QuizResultScreen(
    onNavigationButtonClick: () -> Unit,
    onQuestionClick: (String) -> Unit,
    quizResultViewModel: QuizResultViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by quizResultViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isManager) {
        OwnerQuizResultScreen(
            questions = uiState.questions,
            quizTitle = uiState.quizTitle,
            snackbarHostState = snackbarHostState,
            onNavigationButtonClick = onNavigationButtonClick,
            onQuestionClick = onQuestionClick,
        )
    } else {
        GeneralQuizResultScreen(
            quizTitle = uiState.quizTitle,
            quizResult = uiState.quizResult,
            onNavigationButtonClick = onNavigationButtonClick,
            snackbarHostState = snackbarHostState,
            onQuestionClick = onQuestionClick,
        )
    }

    if (uiState.isLoading) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
            )
        }
    }

    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            quizResultViewModel.shownErrorMessage()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun QuizResultScreen(
        quizTitle: String?,
        quizResult: QuizResult?,
        snackbarHostState: SnackbarHostState,
        onNavigationButtonClick: () -> Unit,
        onQuestionClick: (String) -> Unit,
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = quizTitle ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigationButtonClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.btn_navigation),
                            )
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { innerPadding ->

            quizResult?.let { quizResult ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    // 캐릭터 & 점수
                    QuizResultContent(
                        totalQuestions = quizResult.totalQuestions,
                        correctQuestions = quizResult.correctQuestions,
                    )
                    // 문제 리스트
                    QuestionResultListContent(
                        questionResults = quizResult.questionResults,
                        onQuestionClick = onQuestionClick,
                    )
                }
            }
        }
    }
}

@Composable
fun QuizResultContent(
    totalQuestions: Int,
    correctQuestions: Int,
) {

    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            WeQuizRightChatBubble(text = stringResource(R.string.txt_quiz_result_guide))

            WeQuizRightChatBubble(
                text = stringResource(
                    R.string.txt_quiz_result_score,
                    correctQuestions,
                    totalQuestions,
                ),
            )
        }

        WeQuizLocalRoundedImage(
            modifier = Modifier.size(120.dp),
            imagePainter = painterResource(id = R.drawable.sample_profile1),
            contentDescription = null,
        )
    }
}

@Composable
fun QuestionResultListContent(
    questionResults: List<QuestionResult>,
    onQuestionClick: (String) -> Unit,
) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = stringResource(R.string.txt_quiz_question_list),
    )

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(items = questionResults, key = { it.choiceQuestion.id }) { questionResult ->
            QuestionResultItem(
                questionResult = questionResult,
                onQuestionClick = onQuestionClick,
            )
        }
    }
}

@Composable
fun QuestionResultItem(
    questionResult: QuestionResult,
    onQuestionClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .clickable(onClick = { onQuestionClick(questionResult.choiceQuestion.id) })
            .padding(10.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        VerticalDivider(
            thickness = 4.dp,
            color = when (questionResult.isCorrect) {
                true -> MaterialTheme.colorScheme.surfaceTint
                false -> MaterialTheme.colorScheme.error
            },
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            val currentQuestion = questionResult.choiceQuestion
            Text(
                text = currentQuestion.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (currentQuestion is ChoiceQuestion) {
                Text(
                    text = currentQuestion.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            // TODO: blank question 처리 해야 해요!!
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.End),
                onClick = { /* no-op */ },
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(showBackground = true)
@Composable
fun QuizResultScreenPreview() {
    WeQuizTheme {
        QuizResultScreen(
            onNavigationButtonClick = {},
            onQuestionClick = {},
        )
    }
}
