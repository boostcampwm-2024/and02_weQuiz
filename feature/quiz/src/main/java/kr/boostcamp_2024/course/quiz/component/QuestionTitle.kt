package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizChatBubble
import kr.boostcamp_2024.course.quiz.R

@Composable
internal fun QuestionTitle(title: String) {
    Column(
        modifier = Modifier,
    ) {
        Text(text = stringResource(R.string.txt_question_detail_title), modifier = Modifier, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(10.dp))
        WeQuizChatBubble(text = title)
    }
}

@PreviewKoLightDark
@Composable
private fun QuestionTitlePreview() {
    WeQuizTheme {
        QuestionTitle(title = "test title")
    }
}
