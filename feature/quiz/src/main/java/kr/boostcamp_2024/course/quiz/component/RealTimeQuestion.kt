package kr.boostcamp_2024.course.quiz.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun RealTimeQuestion(
    isOwner: Boolean = false,
    questions: List<String>,
    selectedIndex: Int,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        questions.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
            ) {
                RadioButton(
                    enabled = isOwner.not(),
                    selected = selectedIndex == index,
                    onClick = null,
                    modifier = Modifier.padding(8.dp),
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically),
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ko")
@Preview(uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, locale = "ko")
@Composable
private fun RealTimeQuestionPreview() {
    RealTimeQuestion(
        questions = listOf("문제1", "문제2", "문제3", "문제4", "문제5"),
        selectedIndex = 0,
    )
}
