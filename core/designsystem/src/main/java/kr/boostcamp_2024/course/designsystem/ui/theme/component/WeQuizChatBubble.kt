package kr.boostcamp_2024.course.designsystem.ui.theme.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
fun WeQuizChatBubble(
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
) {
    Surface(
        modifier = modifier.clip(shape),
        color = backgroundColor,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun WeQuizRightChatBubble(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    text: String,
) {
    WeQuizChatBubble(
        modifier = modifier,
        text = text,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = 20.dp,
            bottomEnd = 8.dp,
        ),
    )
}

@Composable
fun WeQuizLeftChatBubble(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    text: String,
) {
    WeQuizChatBubble(
        modifier = modifier,
        text = text,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = 8.dp,
            bottomEnd = 20.dp,
        ),
    )
}

@Preview
@Composable
fun WeQuizChatBubblePreview() {
    WeQuizTheme {
        WeQuizChatBubble(
            text = "Hello, World!",
            shape = RoundedCornerShape(16.dp),
        )
    }
}

@Preview
@Composable
fun ChatBubbleRightPreview() {
    WeQuizTheme {
        WeQuizRightChatBubble(
            text = "Hello, World!",
        )
    }
}

@Preview
@Composable
fun WeQuizLeftChatBubblePreview() {
    WeQuizTheme {
        WeQuizLeftChatBubble(
            text = "Hello, World!",
        )
    }
}
