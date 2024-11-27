package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlankQuestionDescription(questionContent: List<Map<String, String>>) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        questionContent.forEachIndexed { index, content ->
            if (content["type"] == "text") {
                ConsumeTextContentUi(
                    word = content.getOrDefault("text", "") as String,
                    onContentRemove = {},
                    onValueChanged = { _, _ -> },
                    removeIconInvisible = false,
                    textFieldEnabled = false,
                    clickableEnabled = false,
                )
            } else {
                ConsumeBlankContentUi(
                    word = content.getOrDefault("text", "") as String,
                    onContentRemove = {},
                    onValueChanged = { _, _ -> },
                    removeIconVisible = false,
                    textFieldEnabled = false,
                    clickableEnabled = false,
                )
            }
        }
    }
}
