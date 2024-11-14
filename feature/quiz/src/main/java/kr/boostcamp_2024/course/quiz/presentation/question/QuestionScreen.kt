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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.Question
import kr.boostcamp_2024.course.quiz.component.QuestionChatBubbleRight
import kr.boostcamp_2024.course.quiz.component.QuestionDialog
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.component.RoundImage
import kr.boostcamp_2024.course.quiz.viewmodel.QuestionViewModel

@Composable
fun QuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: () -> Unit,
    viewModel: QuestionViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 10 },
    )
    var currentPage by remember { mutableIntStateOf(0) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedIndexList by remember { mutableStateOf(List(10) { -1 }) }
    var isSubmitting by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                QuestionTopBar { showDialog = true }
            }
            item {
                LinearProgressIndicator(
                    progress = { (currentPage + 1) / pagerState.pageCount.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    QuestionChatBubbleRight(20, Modifier.align(Alignment.Center))
                    RoundImage(Modifier.align(Alignment.CenterEnd))
                }
            }
            item {
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                ) {
                    Column {
                        QuestionTitleAndDetail(
                            // TODO (문제 제목, 설명 받아오기)
                            title = "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음.",
                            description = "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음.",
                        )
                        Question(
                            selectedIndex = selectedIndexList[currentPage],
                            onOptionSelected = { newIndex ->
                                selectedIndex = newIndex
                                selectedIndexList = selectedIndexList.toMutableList().apply {
                                    this[currentPage] = newIndex
                                }
                            },
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
                .padding(10.dp),
        ) {
            Button(
                onClick = {
                    if (currentPage < pagerState.pageCount - 1) {
                        currentPage++
                    } else {
                        showDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (currentPage == pagerState.pageCount - 1) {
                        stringResource(R.string.txt_question_done)
                    } else {
                        stringResource(R.string.txt_question_next_question)
                    },
                )
            }
            Button(
                onClick = {
                    if (currentPage > 0) {
                        currentPage--
                    }
                },
                enabled = currentPage > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                ),
            ) {
                Text(text = "이전 문제")
            }
        }
    }

    if (showDialog) {
        QuestionDialog(
            title = if (currentPage == pagerState.pageCount - 1) {
                stringResource(R.string.dialog_submit_script)
            } else {
                stringResource(R.string.dialog_exit_script)
            },
            yesTitle = if (currentPage == pagerState.pageCount - 1) {
                stringResource(R.string.txt_question_submit)
            } else {
                stringResource(R.string.txt_question_exit)
            },
            noTitle = stringResource(R.string.txt_question_cancel),
            onConfirm = {
                showDialog = false
                if (currentPage == pagerState.pageCount - 1) {
                    isSubmitting = true
                    /*
                    TODO (제출 버튼 눌렀을 때)
                    네트워크 통신 진행 - selectedIndexList 전달
                    통신 완료 후 onQuizFinished() 실행(isSubmitting = false로 변경)
                     */
                } else {
                    isSubmitting = true
                    /*
                    TODO (나가기 버튼 눌렀을 때)
                    네트워크 통신 진행 - selectedIndexList 전달
                    통신 완료 후 onNavigationButtonClick() 실행(isSubmitting = false로 변경)
                     */
                    onNavigationButtonClick()
                }
            },
            onDismissRequest = { showDialog = false },
        )
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
