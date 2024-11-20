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
    isOwner: Boolean,
    isWaiting: Boolean,
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
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onCreateQuestionButtonClick(quiz.id) },
                enabled = quiz.isStarted.not(),
            ) {
                when (quiz.isStarted.not()) {
                    true -> Text(text = stringResource(R.string.txt_open_create_question))
                    false -> Text(text = stringResource(R.string.txt_close_create_question))
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onStartQuizButtonClick(quiz.id) },
                enabled = true,
            ) {
                if (quiz.questions.isEmpty()) { // (참여자, 관리자) -> 문제가 없는 경우
                    Text(text = stringResource(R.string.txt_quiz_question_count_zero))
                } else if (isOwner && quiz.isStarted.not()) { // (관리자) -> 퀴즈 시작 전
                    Text(text = "시작 하기(대기: ${quiz.waitingUsers}명)")
                } else if (isOwner.not() && quiz.isStarted.not()) { // (참여자) -> 퀴즈 시작 전
                    Text(text = "대기하기")
                } else if (isOwner.not() && quiz.isStarted.not() && isWaiting) { // (참여자) -> 대기 중
                    Text(text = "대기: ${quiz.waitingUsers}명")
                } else if (quiz.isStarted && quiz.isFinished.not()) { // (참여자, 관리자) -> 퀴즈 진행 중
                    Text(text = "퀴즈 진행 중")
                } else if (quiz.isFinished) { // (참여자, 관리자) -> 퀴즈 종료
                    Text(text = "시험이 끝났습니다.")
                }
            }
        }

        null -> {}
    }
}
