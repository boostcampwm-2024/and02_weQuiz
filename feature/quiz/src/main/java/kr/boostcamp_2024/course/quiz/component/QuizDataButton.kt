package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.quiz.R

@Composable
fun QuizDataButton(
    quiz: BaseQuiz?,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: (String) -> Unit,
) {
    when (quiz) {
        is Quiz -> {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onCreateQuestionButtonClick(quiz.id) },
                enabled = quiz.isOpened.not(),
            ) {
                when (quiz.isOpened.not()) {
                    true -> Text(text = stringResource(R.string.txt_open_create_question))
                    false -> Text(text = stringResource(R.string.txt_close_create_question))
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onStartQuizButtonClick(quiz.id) },
                enabled = (quiz.isOpened && quiz.questions.isNotEmpty()),
            ) {
                when (quiz.isOpened && quiz.questions.isEmpty()) {
                    true -> Text(text = stringResource(R.string.txt_quiz_question_count_zero))
                    false -> Text(text = stringResource(R.string.txt_quiz_start))
                }
            }
        }

        is RealTimeQuiz -> {

        }

        null -> {}
    }
}
