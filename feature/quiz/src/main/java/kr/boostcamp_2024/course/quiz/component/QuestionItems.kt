package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


@Composable
fun QuestionItems(choice: List<String>, answer: Int, onClick: () -> Unit) {
    Column(modifier = Modifier.selectableGroup()) {
        val selectedType by remember { mutableIntStateOf(answer) }
        choice.forEachIndexed { idx, it ->
            RadioTextButton(
                text = it,
                selected = selectedType == idx,
                onclick = onClick
            )
        }
    }
}