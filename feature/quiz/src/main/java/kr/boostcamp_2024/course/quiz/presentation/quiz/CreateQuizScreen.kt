package kr.boostcamp_2024.course.quiz.presentation.quiz

import WeQuizPhotoPickerAsyncImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizValidateTextField
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.QuizDatePickerTextField
import kr.boostcamp_2024.course.quiz.component.QuizSolveTimeSlider
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuizViewModel

@Composable
fun CreateQuizScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuizSuccess: () -> Unit,
    onEditQuizSuccess: () -> Unit,
    viewModel: CreateQuizViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    CreateQuizScreen(
        quizTitle = uiState.value.quizTitle,
        quizDescription = uiState.value.quizDescription,
        quizDate = uiState.value.quizDate,
        quizSolveTime = uiState.value.quizSolveTime,
        createQuizButtonEnabled = uiState.value.isCreateQuizButtonEnabled && !uiState.value.isLoading,
        isEditing = uiState.value.isEditing,
        selectedQuizTypeIndex = uiState.value.selectedQuizTypeIndex,
        snackBarHostState = snackBarHostState,
        defaultImageUrl = uiState.value.defaultImageUrl,
        onQuizTitleChange = viewModel::setQuizTitle,
        onQuizDescriptionChange = viewModel::setQuizDescription,
        onQuizDateChange = viewModel::setQuizDate,
        onQuizSolveTimeChange = viewModel::setQuizSolveTime,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateQuizButtonClick = viewModel::createQuiz,
        onEditButtonClick = viewModel::editQuiz,
        currentImage = uiState.value.currentImage,
        onCurrentStudyImageChanged = viewModel::changeCurrentStudyImage,
        isRealtimeQuiz = uiState.value.isRealtimeQuiz,
        onQuizTypeIndexChange = viewModel::setSelectedQuizTypeIndex,
    )

    if (uiState.value.isCreateQuizSuccess) {
        LaunchedEffect(Unit) {
            onCreateQuizSuccess()
        }
    }

    if (uiState.value.isEditQuizSuccess) {
        LaunchedEffect(Unit) {
            onEditQuizSuccess()
        }
    }

    if (uiState.value.isLoading) {
        WeQuizCircularProgressIndicator()
    }

    LaunchedEffect(uiState.value.snackBarMessage) {
        uiState.value.snackBarMessage?.let { snackBarMessage ->
            snackBarHostState.showSnackbar(snackBarMessage)
            viewModel.shownErrorMessage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    quizTitle: String,
    quizDescription: String,
    quizDate: String,
    quizSolveTime: Float,
    createQuizButtonEnabled: Boolean,
    isEditing: Boolean,
    selectedQuizTypeIndex: Int,
    isRealtimeQuiz: Boolean,
    currentImage: ByteArray?,
    snackBarHostState: SnackbarHostState,
    defaultImageUrl: String?,
    onQuizTitleChange: (String) -> Unit,
    onQuizDescriptionChange: (String) -> Unit,
    onQuizDateChange: (String) -> Unit,
    onQuizSolveTimeChange: (Float) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onCreateQuizButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    onCurrentStudyImageChanged: (ByteArray) -> Unit,
    onQuizTypeIndexChange: (Int) -> Unit,
) {
    val options = listOf(stringResource(R.string.txt_create_quiz_general), stringResource(R.string.txt_create_quiz_realtime))

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (isEditing) {
                        Text(text = stringResource(R.string.top_app_bar_edit_quiz))
                    } else {
                        Text(text = stringResource(R.string.top_app_bar_create_quiz))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.btn_navigation),
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        onClick = { onQuizTypeIndexChange(index) },
                        selected = index == selectedQuizTypeIndex,
                    ) {
                        Text(
                            text = label,
                        )
                    }
                }
            }
            WeQuizPhotoPickerAsyncImage(
                imageData = currentImage ?: defaultImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 70.dp, vertical = 5.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(18.dp)),
                onImageDataChanged = onCurrentStudyImageChanged,
            )

            // QuizInfo
            // Title
            WeQuizValidateTextField(
                text = quizTitle,
                onTextChanged = onQuizTitleChange,
                label = stringResource(R.string.txt_quiz_title_label),
                placeholder = stringResource(R.string.txt_quiz_title_placeholder),
                errorMessage = stringResource(R.string.txt_quiz_title_error_message),
                validFun = { it.length <= 20 },
            )

            //Description
            WeQuizValidateTextField(
                text = quizDescription,
                onTextChanged = onQuizDescriptionChange,
                label = stringResource(R.string.txt_quiz_description_label),
                placeholder = stringResource(R.string.txt_quiz_description_placeholder),
                minLines = 6,
                maxLines = 6,
                errorMessage = stringResource(R.string.txt_quiz_description_error_message),
                validFun = { it.length <= 100 },
            )
            if (!isRealtimeQuiz) {

                // StartTime
                Text(text = stringResource(R.string.txt_quiz_start_time))
                QuizDatePickerTextField(
                    quizDate = quizDate,
                    onDateSelected = { onQuizDateChange(it) },
                )

                // SolveTime
                Text(text = stringResource(R.string.txt_quiz_solve_time))
                QuizSolveTimeSlider(
                    value = quizSolveTime,
                    steps = 8,
                    valueRange = 10f..100f,
                    onValueChange = { onQuizSolveTimeChange(it) },
                )
            }
            if (isEditing) {
                // EditButton
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onEditButtonClick,
                    enabled = createQuizButtonEnabled,
                ) {
                    Text(text = stringResource(R.string.btn_edit_quiz))
                }
            } else {
                // CreateButton
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateQuizButtonClick,
                    enabled = createQuizButtonEnabled,
                ) {
                    Text(text = stringResource(R.string.btn_create_quiz))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateQuizScreenPreview() {
    WeQuizTheme {
        CreateQuizScreen(
            quizTitle = "",
            quizDescription = "",
            quizDate = "",
            quizSolveTime = 10f,
            createQuizButtonEnabled = true,
            isEditing = false,
            isRealtimeQuiz = false,
            currentImage = null,
            snackBarHostState = remember { SnackbarHostState() },
            defaultImageUrl = null,
            onQuizTitleChange = {},
            onQuizDescriptionChange = {},
            onQuizDateChange = {},
            onQuizSolveTimeChange = {},
            onNavigationButtonClick = {},
            onCreateQuizButtonClick = {},
            onEditButtonClick = {},
            onCurrentStudyImageChanged = {},
            onQuizTypeIndexChange = {},
            selectedQuizTypeIndex = 0,
        )
    }
}
