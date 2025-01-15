package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
internal fun Question(
    questions: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        questions.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .selectable(
                        selected = selectedIndex == index,
                        onClick = { onOptionSelected(index) },
                    ),
            ) {
                RadioButton(
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

@Preview(showBackground = true)
@Composable
private fun QuestionPreview() {
    WeQuizTheme {
        Question(
            questions = listOf("A", "B", "C", "D"),
            selectedIndex = 0,
            onOptionSelected = { },
        )
    }

}
