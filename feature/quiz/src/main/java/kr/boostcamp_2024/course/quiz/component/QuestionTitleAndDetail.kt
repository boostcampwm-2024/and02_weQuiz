package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.R

@Composable
internal fun QuestionTitleAndDetail(title: String, description: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 10.dp),
    ) {
        Text(
            stringResource(R.string.txt_question_title),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        QuestionTextBox(
            text = title,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        )
        Text(
            stringResource(R.string.txt_question_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        QuestionTextBox(
            text = description,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        )
        HorizontalDivider()
    }
}

@Preview(locale = "ko")
@PreviewLightDark
@Composable
private fun QuestionTitleAndDetailPreview() {
    WeQuizTheme {
        QuestionTitleAndDetail(
            "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. ",
            "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. ",
        )
    }
}
