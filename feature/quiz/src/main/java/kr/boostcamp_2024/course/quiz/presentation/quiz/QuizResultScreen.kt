package kr.boostcamp_2024.course.quiz

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    onNavigationButtonClick: () -> Unit,
    onQuestionClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.top_app_bar_quiz_result)) },
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
        ) {
            QuizResultContent( // 캐릭터 & 점수
                totalQuestions = 10,
                correctAnswers = 9
            )
            QuestionResultListContent( // 문제 리스트
                onQuestionClick = onQuestionClick
            )
        }
    }
}

@Composable
fun QuizResultContent(
    totalQuestions: Int,
    correctAnswers: Int
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ChatBubbleRight(text = stringResource(R.string.txt_quiz_result_guide))
            ChatBubbleRight(
                text = stringResource(
                    R.string.txt_quiz_result_score,
                    correctAnswers,
                    totalQuestions
                )
            )
        }

        CircleImage(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterVertically),
            imagePainter = painterResource(id = R.drawable.sample_profile),
            contentDescription = null
        )
    }
}

@Composable
fun QuestionResultListContent(
    onQuestionClick: () -> Unit
) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = stringResource(R.string.txt_quiz_question_list)
    )

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            QuestionResultItem(
                questionId = 1,
                isCorrect = true,
                questionTitle = "아파트 아파트~",
                questionImageUrl = "",
                questionDescription = "채영이가 좋아하는 랜덤 게임",
                onQuestionClick = onQuestionClick
            )
        }

        item {
            QuestionResultItem(
                questionId = 1,
                isCorrect = false,
                questionTitle = "거침없이 걸어가지",
                questionImageUrl = "123",
                questionDescription = "삐리뽕빠라빵",
                onQuestionClick = onQuestionClick
            )
        }

        item {
            QuestionResultItem(
                questionId = 1,
                isCorrect = true,
                questionTitle = "그런 일은 절대로 없을 거라",
                questionImageUrl = "",
                questionDescription = "그런 일은 절대로 없는 거죠, 나는 믿을게요 오늘은 안 돼요, 내 사랑이 이대로는 이별을 감당하긴 어려운걸요",
                onQuestionClick = onQuestionClick
            )
        }
    }
}

@Composable
fun QuestionResultItem(
    questionId: Int,
    isCorrect: Boolean,
    questionTitle: String,
    questionImageUrl: String,
    questionDescription: String,
    onQuestionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .clickable(onClick = onQuestionClick)
            .padding(10.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        VerticalDivider(
            thickness = 4.dp,
            color = if (isCorrect) Color(0xFF01A581) else Color(0xFFFF6A62)
        )

        // TODO QuestionImageComposable
        if (questionImageUrl.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = questionTitle,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = questionDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.End),
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ChatBubbleRight(
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 8.dp
                )
            ),
        color = backgroundColor
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CircleImage(
    imagePainter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .clip(CircleShape),
        painter = imagePainter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
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