package kr.boostcamp_2024.course.quiz.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import kr.boostcamp_2024.course.domain.model.BaseQuiz

@Composable
fun QuizDataText(
    quiz: BaseQuiz?,
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
}
