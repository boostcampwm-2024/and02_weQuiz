package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.component.QuestionDescription
import kr.boostcamp_2024.course.quiz.component.QuestionDetailTopAppBar
import kr.boostcamp_2024.course.quiz.component.QuestionItems
import kr.boostcamp_2024.course.quiz.component.QuestionSolution
import kr.boostcamp_2024.course.quiz.component.QuestionTitle
import kr.boostcamp_2024.course.quiz.presentation.quiz.QuizStatisticsDialog
import kr.boostcamp_2024.course.quiz.viewmodel.QuestionDetailViewModel

@Composable
fun QuestionDetailScreen(
    viewModel: QuestionDetailViewModel = hiltViewModel<QuestionDetailViewModel>(),
    onNavigationButtonClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuestionDetailScreen(
        title = uiState.title,
        description = uiState.description,
        choices = uiState.choices,
        answer = uiState.answer,
        solution = uiState.solution,
        errorMessage = uiState.errorMessage,
        onNavigationButtonClick = onNavigationButtonClick,
        onErrorMessageShown = viewModel::shownErrorMessage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionDetailScreen(
    onNavigationButtonClick: () -> Unit,
    title: String,
    description: String,
    choices: List<String>,
    answer: Int,
    solution: String?,
    errorMessage: String?,
    onErrorMessageShown: () -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { QuestionDetailTopAppBar(onNavigationButtonClick = onNavigationButtonClick) },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier.padding(end = 16.dp, bottom = 53.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = MaterialTheme.shapes.large,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "퀴즈 결과 화면 보기 버튼",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                text = {
                    Text(
                        text = "결과 보기",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 10.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            QuestionTitle(title)

            QuestionDescription(description)

            QuestionItems(choices, answer) {}

            QuestionSolution(solution)

            if (showDialog) {
                QuizStatisticsDialog(
                    onConfirmButtonClick = { showDialog = false },
                    onDismissRequest = { showDialog = false },
                    quizId = "123",
                )
            }
        }
        if (errorMessage != null) {
            LaunchedEffect(errorMessage) {
                snackBarHostState.showSnackbar(errorMessage)
                onErrorMessageShown()
            }
        }
    }
}

private fun getPreviewQuestion(): Question {
    // TODO 뷰모델과 연결하고 임시값 빼기
    return Question(
        id = "",
        "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. ",
        "제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. 제목 전체 다 보여줌. 줄 수 상관 없음. ",
        "해설이 들어갑니다... 넙적한 건 어쩔 수 없는듯... 들어갈 수 있는 양 모두 들어감. 친구가 해설을 길게 쓴다면 그 내용 다 들어감... 해설이 들어갑니다... 넙적한 건 어쩔 수 없는듯... 들어갈 수 있는 양 모두 들어감. 친구가 해설을 길게 쓴다면 그 내용 다 들어감... ",
        0,
        listOf(
            "1번 객관식 문항 내용입니다. 이것도 전체 다 보여줌. 1번 객관식 문항입니다. 이것도 전체",
            "1번 객관식 문항 내용입니다. 이것도 전체 다 보여줌. 1번 객관식 문항입니다. 이것도 전체",
            "1번 객관식 문항 내용입니다. 이것도 전체 다 보여줌. 1번 객관식 문항입니다. 이것도 전체",
            "1번 객관식 문항 내용입니다. 이것도 전체 다 보여줌. 1번 객관식 문항입니다. 이것도 전체",
        ),
        currentSubmit = 0,
    )
}

@Preview
@Composable
fun QuestionDetailScreenPreview() {
    val question = getPreviewQuestion()
    WeQuizTheme {
        QuestionDetailScreen(
            onNavigationButtonClick = {},
            title = question.title,
            description = question.description,
            choices = question.choices,
            answer = question.answer,
            solution = question.solution,
            errorMessage = null,
        )
    }
}
