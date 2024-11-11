package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizLocalRoundedImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizRightChatBubble
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.Question
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.utils.formatTime

data class QuestionScreenState(
    val currentPage: Int = 0,
    val selectedIndexList: List<Int> = List(10) { -1 },
    val showDialog: Boolean = false,
    val isSubmitting: Boolean = false,
    val countDownTime: Int = 20 * 60
)

@Composable
fun QuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 10 })
    var state by remember { mutableStateOf(QuestionScreenState()) }
    LaunchedEffect(Unit) {
        while (state.countDownTime > 0) {
            delay(1000L)
            state = state.copy(countDownTime = state.countDownTime - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                QuestionTopBar { state = state.copy(showDialog = true) }
            }
            item {
                LinearProgressIndicator(
                    progress = { (state.currentPage + 1) / pagerState.pageCount.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    WeQuizRightChatBubble(
                        modifier = Modifier.align(Alignment.Center),
                        text = "${stringResource(R.string.txt_question_timer)}${formatTime(state.countDownTime)}",
                    )
                    WeQuizLocalRoundedImage(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imagePainter = painterResource(id = R.drawable.quiz_system_profile),
                        contentDescription = stringResource(R.string.des_image_question),
                    )
                }
            }
            item {
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false
                ) {
                    Column {
                        QuestionTitleAndDetail(
                            // TODO (문제 제목, 설명 받아오기)
                            title = "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음.",
                            description = "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음."
                        )
                        Question(
                            selectedIndex = state.selectedIndexList[state.currentPage],
                            onOptionSelected = { newIndex ->
                                val newList = state.selectedIndexList.toMutableList().apply {
                                    this[state.currentPage] = newIndex
                                }
                                state = state.copy(selectedIndexList = newList)
                            }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Transparent)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(
                onClick = {
                    state = if (state.currentPage < pagerState.pageCount - 1) {
                        state.copy(currentPage = state.currentPage + 1)
                    } else {
                        state.copy(showDialog = true)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (state.currentPage == pagerState.pageCount - 1)
                        stringResource(R.string.txt_question_done)
                    else
                        stringResource(R.string.txt_question_next_question)
                )
            }
            Button(
                onClick = {
                    if (state.currentPage > 0) {
                        state = state.copy(currentPage = state.currentPage - 1)
                    }
                },
                enabled = state.currentPage > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                )
            ) {
                Text(text = "이전 문제")
            }
        }
    }

    if (state.showDialog) {
        WeQuizBaseDialog(
            title = if (state.currentPage == pagerState.pageCount - 1)
                stringResource(R.string.dialog_submit_script)
            else
                stringResource(R.string.dialog_exit_script),
            confirmTitle = if (state.currentPage == pagerState.pageCount - 1)
                stringResource(R.string.txt_question_submit)
            else
                stringResource(R.string.txt_question_exit),
            dismissTitle = stringResource(R.string.txt_question_cancel),
            onConfirm = {
                state = state.copy(showDialog = false, isSubmitting = true)
                if (state.currentPage == pagerState.pageCount - 1) {
                    /*
                    TODO (제출 버튼 눌렀을 때)
                     네트워크 통신 진행 - selectedIndexList 전달
                     통신 완료 후 onQuizFinished() 실행(isSubmitting = false로 변경)
                     */
                } else {
                    /*
                    TODO (나가기 버튼 눌렀을 때)
                     네트워크 통신 진행 - selectedIndexList 전달
                     통신 완료 후 onNavigationButtonClick() 실행(isSubmitting = false로 변경)
                     */
                    onNavigationButtonClick()
                }
            },
            onDismissRequest = { state = state.copy(showDialog = false) },
            dialogImage = painterResource(id = R.drawable.quiz_system_profile)
        ) { }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionScreenPreview() {
    QuestionScreen(
        onNavigationButtonClick = {},
        onQuizFinished = {},
    )
}