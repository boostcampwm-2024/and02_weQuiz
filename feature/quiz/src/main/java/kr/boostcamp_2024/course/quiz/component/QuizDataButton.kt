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
    currentUserId: String?,
    onCreateQuestionButtonClick: (String) -> Unit,
    onStartQuizButtonClick: (String) -> Unit,
    onStartRealTimeQuizButtonClick: () -> Unit,
    onWaitingRealTimeQuizButtonClick: (Boolean) -> Unit,
) {
    currentUserId?.let { currentUserId ->
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
                val isOwner = quiz.ownerId == currentUserId
                val isWaiting = quiz.waitingUsers.contains(currentUserId)

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onCreateQuestionButtonClick(quiz.id) },
                    enabled = quiz.isStarted.not() && isWaiting.not(),
                ) {
                    when (quiz.isStarted.not()) {
                        true -> Text(text = stringResource(R.string.txt_open_create_question))
                        false -> Text(text = stringResource(R.string.txt_close_create_question))
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (isOwner && quiz.isStarted.not() && quiz.waitingUsers.isNotEmpty() && quiz.questions.isNotEmpty()) { // (관리자) -> 퀴즈 시작 전
                            onStartRealTimeQuizButtonClick()
                        } else if (isOwner.not() && quiz.isStarted.not() && isWaiting.not() && quiz.questions.isNotEmpty()) { // (참여자) -> 퀴즈 시작 전
                            onWaitingRealTimeQuizButtonClick(true)
                        }
                    },
                    enabled =
                        if (isOwner && quiz.isStarted.not() && quiz.waitingUsers.isNotEmpty() && quiz.questions.isNotEmpty()) { // (관리자) -> 퀴즈 시작 전
                            true
                        } else if (isOwner.not() && quiz.isStarted.not() && isWaiting.not() && quiz.questions.isNotEmpty()) { // (참여자) -> 퀴즈 시작 전
                            true
                        } else {
                            false
                        },
                ) {
                    if (quiz.questions.isEmpty()) { // (참여자, 관리자) -> 문제가 없는 경우
                        Text(text = stringResource(R.string.txt_quiz_question_count_zero))
                    } else if (isOwner && quiz.isStarted.not()) { // (관리자) -> 퀴즈 시작 전
                        Text(text = stringResource(R.string.txt_real_time_quiz_owner, quiz.waitingUsers.size))
                    } else if (isOwner.not() && quiz.isStarted.not() && isWaiting.not()) { // (참여자) -> 퀴즈 시작 전
                        Text(text = stringResource(R.string.txt_real_time_quiz_wait_false))
                    } else if (isOwner.not() && quiz.isStarted.not() && isWaiting) { // (참여자) -> 대기 중
                        Text(text = stringResource(R.string.txt_real_time_quiz_wait_true, quiz.waitingUsers.size))
                    } else if (quiz.isStarted && quiz.isFinished.not()) { // (참여자, 관리자) -> 퀴즈 진행 중
                        Text(text = stringResource(R.string.txt_real_time_quiz_progressing))
                    } else if (quiz.isFinished) { // (참여자, 관리자) -> 퀴즈 종료
                        Text(text = stringResource(R.string.txt_real_time_quiz_finished))
                    }
                }
            }

            null -> { // no-op
            }
        }
    }
}
