package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import kr.boostcamp_2024.course.quiz.R

@Composable
fun QuestionTitleAndDetail(title: String, detail: String, modifier: Modifier) {
    Column {
        Text(
            stringResource(R.string.txt_question_title),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier,
        )
        QuestionTextBox(text = title, modifier = modifier)
        Text(
            stringResource(R.string.txt_question_description),
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