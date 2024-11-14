package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizChatBubble
import kr.boostcamp_2024.course.quiz.R

@Composable
fun QuestionDescription(description: String) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(text = stringResource(R.string.txt_question_detail_description), modifier = Modifier, style = MaterialTheme.typography.bodyMedium)
        WeQuizChatBubble(text = description)
    }
    HorizontalDivider(modifier = Modifier)
}
