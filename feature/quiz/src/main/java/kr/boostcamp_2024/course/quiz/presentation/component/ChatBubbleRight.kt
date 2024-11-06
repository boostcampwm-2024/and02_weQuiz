package kr.boostcamp_2024.course.quiz.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubbleLeft(
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 20.dp
                )
            ),
        color = backgroundColor
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
