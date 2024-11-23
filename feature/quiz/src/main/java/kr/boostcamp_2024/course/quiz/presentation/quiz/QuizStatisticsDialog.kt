package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
fun QuizStatisticsDialog(
    onConfirmButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    quizId: String,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.padding(top = 13.dp, start = 24.dp, end = 24.dp),
                    text = "퀴즈 결과 통계",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                // 차트 그리기
                Box(
                    modifier = Modifier.size(120.dp),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmButtonClick,
            ) {
                Text(text = "닫기")
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizStaticsDialogPreview() {
    WeQuizTheme {
        QuizStatisticsDialog(
            onConfirmButtonClick = {},
            quizId = "1234",
            onDismissRequest = {},
        )
    }
}
