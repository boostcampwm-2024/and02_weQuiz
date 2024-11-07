package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun QuestionTitleAndDetail(title: String, detail: String, modifier: Modifier) {
    Column {
        Text(
            "제목",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier,
        )
        QuestionTextBox(text = title, modifier = modifier)
        Text(
            "설명",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier,
        )
        QuestionTextBox(text = detail, modifier = modifier)
        HorizontalDivider(
            modifier = modifier,

            )
    }
}