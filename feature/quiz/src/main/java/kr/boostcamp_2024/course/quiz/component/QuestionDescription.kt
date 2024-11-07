package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuestionDescription(description: String) {
    Column(
        modifier = Modifier.padding(
            start = 16.dp, end = 16.dp, top = 0.dp, bottom = 10.dp
        )
    ) {
        Text(text = "설명", modifier = Modifier, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(10.dp))
        ChatBubble(description)
    }

    Spacer(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(Color(0xFFBFC9C3))
    )
}