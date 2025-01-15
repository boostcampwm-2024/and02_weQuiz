package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.presentation.question.PieChartScreen

@Composable
fun QuizStatisticsDialog(
    onConfirmButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    userAnswer: List<Int>,
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
                    text = stringResource(R.string.txt_quiz_statistics_dialog),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                PieChartScreen(userAnswers = userAnswer)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmButtonClick,
            ) {
                Text(text = stringResource(R.string.btn_quiz_statistics_dialog_close))
            }
        },
    )
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun QuizStaticsDialogPreview() {
    WeQuizTheme {
        QuizStatisticsDialog(
            onConfirmButtonClick = {},
            userAnswer = listOf(1, 2, 3, 4),
            onDismissRequest = {},
        )
    }
}
