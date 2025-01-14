package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(10.dp),
            )
            .background(MaterialTheme.colorScheme.secondaryContainer),
    ) {

        Text(
            text = text,
            modifier = Modifier.padding(
                10.dp,
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
