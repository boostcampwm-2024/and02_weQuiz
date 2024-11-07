package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.quiz.component.Question
import kr.boostcamp_2024.course.quiz.component.QuestionDialog
import kr.boostcamp_2024.course.quiz.component.QuestionTitleAndDetail
import kr.boostcamp_2024.course.quiz.component.QuestionTopBar
import kr.boostcamp_2024.course.quiz.component.RoundImage

@Composable
fun QuestionScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizFinished: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 10 }
    )
    var currentPage by remember { mutableIntStateOf(0) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedIndexList by remember { mutableStateOf(List(10) { -1 }) }
    var isSubmitting by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 120.dp)
        ) {
            QuestionTopBar { showDialog = true }
            LinearProgressIndicator(
                progress = { (currentPage + 1) / pagerState.pageCount.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                //TODO (문제 풀이 시간 받아서 solveTime에 넣기)
                QuestionChatBubbleRight(20, Modifier.align(Alignment.Center))
                RoundImage(Modifier.align(Alignment.CenterEnd))
            }
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) {
                Column {
                    QuestionTitleAndDetail(
                        //TODO (문제 제목, 설명 받아오기)
                        "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. ",
                        "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. ",
                        modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                    )
                    Question(
                        modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                        selectedIndex = selectedIndexList[currentPage],
                        onOptionSelected = { newIndex ->
                            selectedIndex = newIndex
                            selectedIndexList = selectedIndexList.toMutableList().apply {
                                this[currentPage] = newIndex
                            }
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.White.copy(alpha = 0.8f))
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(
                onClick = {
                    if (currentPage < pagerState.pageCount - 1) {
                        currentPage++
                    } else {
                        //question dialog 띄우기
                        showDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (currentPage == pagerState.pageCount - 1) "완료" else "다음 문제")
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
                    .padding(vertical = 5.dp)
            ) {
                Text(text = "이전 문제")
            }
            Text(
                text = "${currentPage + 1}/10",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
    if (showDialog) {
        QuestionDialog(
            title = if (currentPage == pagerState.pageCount - 1)
                "최종 제출하시나요?\n제출 후 답을 수정할 수 없습니다."
            else
                "정말 나가시겠습니까?\n진행 상황이 저장되지 않을 수 있습니다.",
            yesTitle = if (currentPage == pagerState.pageCount - 1) "제출" else "나가기",
            noTitle = if (currentPage == pagerState.pageCount - 1) "취소" else "취소",
            onConfirm = {
                showDialog = false
                if (currentPage == pagerState.pageCount - 1) {
                    isSubmitting = true
                    //TODO (제출 버튼 눌렀을 때)
                    //네트워크 통신 진행 - selectedIndexList 전달
                    //통신 완료 후 onQuizFinished() 실행(isSubmitting = false로 변경)
                    //백그라운드에서 실행시키고 dialog로 로딩 창 실행
                } else {
                    isSubmitting = true
                    //TODO (나가기 버튼 눌렀을 때)
                    //네트워크 통신 진행 - selectedIndexList 전달
                    //통신 완료 후 onNavigationButtonClick() 실행(isSubmitting = false로 변경)
                    //백그라운드에서 실행시키고 dialog로 로딩 창 실행
                    onNavigationButtonClick()
                }
            },
            onDismissRequest = { showDialog = false }
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