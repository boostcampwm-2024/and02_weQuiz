package kr.boostcamp_2024.course.quiz.presentation.quiz

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.component.QuizDatePickerTextField
import kr.boostcamp_2024.course.quiz.component.QuizSolveTimeSlider
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuizViewModel
import java.io.ByteArrayOutputStream

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
        createQuizButtonEnabled = uiState.value.isCreateQuizButtonEnabled,
        isEditing = uiState.value.isEditing,
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
        Box {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
            )
        }
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
) {
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val data = baos.toByteArray()
            onCurrentStudyImageChanged(data)
        }
    }

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
            WeQuizAsyncImage(
                imgUrl = currentImage ?: defaultImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 70.dp, vertical = 5.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable(onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }),
                placeholder = painterResource(R.drawable.image_guide),
                error = painterResource(R.drawable.image_guide),
                fallback = painterResource(R.drawable.image_guide),
            )
//            AsyncImage(
//                model = currentImage ?: defaultImageUrl,
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 70.dp, vertical = 5.dp)
//                    .aspectRatio(1f)
//                    .clip(RoundedCornerShape(18.dp))
//                    .clickable(onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }),
//                contentScale = ContentScale.Crop,
//                placeholder = painterResource(R.drawable.image_guide),
//                error = painterResource(R.drawable.image_guide),
//                fallback = painterResource(R.drawable.image_guide),
//            )

            // QuizInfo
            // Title
            WeQuizTextField(
                text = quizTitle,
                onTextChanged = onQuizTitleChange,
                label = stringResource(R.string.txt_quiz_title_label),
                placeholder = stringResource(R.string.txt_quiz_title_placeholder),
            )

            //Description
            WeQuizTextField(
                text = quizDescription,
                onTextChanged = onQuizDescriptionChange,
                label = stringResource(R.string.txt_quiz_description_label),
                placeholder = stringResource(R.string.txt_quiz_description_placeholder),
                minLines = 6,
                maxLines = 6,
            )

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
            currentImage = null,
            onQuizTitleChange = {},
            onQuizDescriptionChange = {},
            onQuizDateChange = {},
            onQuizSolveTimeChange = {},
            onNavigationButtonClick = {},
            onCreateQuizButtonClick = {},
            onEditButtonClick = {},
            snackBarHostState = remember { SnackbarHostState() },
            onCurrentStudyImageChanged = {},
            defaultImageUrl = null,
        )
    }
}
