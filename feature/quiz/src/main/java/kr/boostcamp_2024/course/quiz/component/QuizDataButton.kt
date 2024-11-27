package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { onCreateQuestionButtonClick(quiz.id) },
                    enabled = quiz.isOpened.not(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                    ),
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
                val isOwner = remember(quiz) { quiz.ownerId == currentUserId }
                val isParticipant = remember(quiz) { isOwner.not() }
                val isWaiting = remember(quiz) { quiz.waitingUsers.contains(currentUserId) }
                val isProgress = remember(quiz) { (quiz.isStarted && quiz.isFinished.not()) }
                val isOwnerBeforeQuizStart = remember(quiz) { isOwner && quiz.isStarted.not() }
                val isParticipantBeforeQuizStart = remember(quiz) { isParticipant && quiz.isStarted.not() && isWaiting.not() }
                val isParticipantWaiting = remember(quiz) { isParticipant && quiz.isStarted.not() && isWaiting }
                val isStartRealTimeQuizEnabled = remember(quiz) { isOwner && quiz.isStarted.not() && quiz.waitingUsers.isNotEmpty() && quiz.questions.isNotEmpty() }
                val isWaitingRealTimeQuizEnabled = remember(quiz) { isParticipant && quiz.isStarted.not() && isWaiting.not() && quiz.questions.isNotEmpty() }

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
                        if (isStartRealTimeQuizEnabled) {
                            onStartRealTimeQuizButtonClick
                        } else if (isWaitingRealTimeQuizEnabled) {
                            onWaitingRealTimeQuizButtonClick(true)
                        }
                    },
                    enabled = isStartRealTimeQuizEnabled || isWaitingRealTimeQuizEnabled,
                ) {
                    if (quiz.questions.isEmpty()) { // (참여자, 관리자) -> 문제가 없는 경우
                        Text(text = stringResource(R.string.txt_quiz_question_count_zero))
                    } else if (isOwnerBeforeQuizStart) { // (관리자) -> 퀴즈 시작 전
                        Text(text = stringResource(R.string.txt_real_time_quiz_owner, quiz.waitingUsers.size))
                    } else if (isParticipantBeforeQuizStart) { // (참여자) -> 퀴즈 시작 전
                        Text(text = stringResource(R.string.txt_real_time_quiz_wait_false))
                    } else if (isParticipantWaiting) { // (참여자) -> 대기 중
                        Text(text = stringResource(R.string.txt_real_time_quiz_wait_true, quiz.waitingUsers.size))
                    } else if (isProgress) { // (참여자, 관리자) -> 퀴즈 진행 중
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
