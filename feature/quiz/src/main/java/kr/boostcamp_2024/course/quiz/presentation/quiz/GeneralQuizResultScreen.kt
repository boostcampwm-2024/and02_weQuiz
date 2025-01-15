package kr.boostcamp_2024.course.quiz.presentation.quiz

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.QuestionResult
import kr.boostcamp_2024.course.domain.model.QuizResult
import kr.boostcamp_2024.course.quiz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralQuizResultScreen(
    quizTitle: String?,
    quizResult: QuizResult?,
    onNavigationButtonClick: () -> Unit,
    onQuestionClick: (String) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
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
                GeneralQuizResultContent(
                    totalQuestions = quizResult.totalQuestions,
                    correctQuestions = quizResult.correctQuestions,
                )
                // 문제 리스트
                GeneralQuestionResultListContent(
                    questionResults = quizResult.questionResults,
                    onQuestionClick = onQuestionClick,
                )
            }
        }
    }
}

@Composable
fun GeneralQuizResultContent(
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
            WeQuizRightChatBubble(
                text = stringResource(R.string.txt_quiz_result_guide),
            )
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
fun GeneralQuestionResultListContent(
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
            GeneralQuestionResultItem(
                questionResult = questionResult,
                onQuestionClick = onQuestionClick,
            )
        }
    }
}

@Composable
fun GeneralQuestionResultItem(
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
            Text(
                text = questionResult.choiceQuestion.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (questionResult.choiceQuestion is ChoiceQuestion) {
                Text(
                    text = (questionResult.choiceQuestion as ChoiceQuestion).description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.End),
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
            )
        }
    }
}

@Preview(locale = "ko")
@Preview(uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, locale = "ko")
@Composable
fun GeneralQuizResultScreenPreview() {
    val generalQuizResultPreviewUserOmrAnswers = listOf(0, 1, "")
    WeQuizTheme {
        GeneralQuizResultScreen(
            quizTitle = "퀴즈 타이틀",
            quizResult = QuizResult(
                questions = quizResultPreviewQuestions,
                userOmrAnswers = generalQuizResultPreviewUserOmrAnswers,
            ),
            onNavigationButtonClick = {},
            onQuestionClick = { _ -> },
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
