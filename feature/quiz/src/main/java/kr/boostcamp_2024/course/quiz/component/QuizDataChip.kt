package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.utils.QuizParameterProvider

@Composable
internal fun QuizDataChip(
    category: Category?,
    quiz: BaseQuiz?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
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

        if (quiz is RealTimeQuiz) {
            ElevatedAssistChip(
                onClick = { /* no-op */ },
                label = {
                    Text(text = stringResource(R.string.txt_real_time_quiz))
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
}

@PreviewKoLightDark
@Composable
private fun QuizDataChipPreview(
    @PreviewParameter(QuizParameterProvider::class) quiz: BaseQuiz,
) {
    WeQuizTheme {
        QuizDataChip(
            quiz = quiz,
            category = Category(
                id = "id",
                name = "category name",
                description = "description",
                categoryImageUrl = "categoryImageUrl",
                quizzes = emptyList(),
            ),
        )
    }
}
