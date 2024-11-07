package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kr.boostcamp_2024.course.quiz.utils.formatTime

@Composable
fun QuestionChatBubbleRight(solveTime: Int, modifier: Modifier) {
    var countDownTime by remember { mutableIntStateOf(solveTime * 60) }

    LaunchedEffect(Unit) {
        while (countDownTime > 0) {
            delay(1000L)
            countDownTime--
        }
    }
    val formattedTime = formatTime(countDownTime)
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 8.dp
                )
            )
            .background(Color(0xFFCEE9DD))
    ) {
        Text(
            text = "⏰$formattedTime",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}