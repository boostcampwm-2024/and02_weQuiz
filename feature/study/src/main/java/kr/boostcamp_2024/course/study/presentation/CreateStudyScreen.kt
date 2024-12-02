package kr.boostcamp_2024.course.study.presentation

import WeQuizPhotoPickerAsyncImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizValidateTextField
import kr.boostcamp_2024.course.study.CreateStudyViewModel
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CreateStudyTopAppBar
import kr.boostcamp_2024.course.study.component.StudySubmitButton

@Composable
fun CreateStudyScreen(
    viewmodel: CreateStudyViewModel = hiltViewModel<CreateStudyViewModel>(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigationButtonClick: () -> Unit,
    onSubmitStudySuccess: () -> Unit,
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    CreateStudyScreen(
        isEditMode = uiState.isEditMode,
        defaultStudyImageUri = uiState.defaultImageUri,
        currentStudyImage = uiState.currentImage,
        titleText = uiState.name,
        descriptionText = uiState.description,
        groupMemberNumber = uiState.maxUserNum,
        canSubmitStudy = uiState.canSubmitStudy && !uiState.isLoading,
        snackBarHostState = snackBarHostState,
        onNavigationButtonClick = onNavigationButtonClick,
        onTitleTextChange = viewmodel::onNameChanged,
        onDescriptionTextChange = viewmodel::onDescriptionChanged,
        onMaxUserNumChange = viewmodel::onMaxUserNumChange,
        onStudyEditButtonClick = viewmodel::updateStudyGroup,
        onCreationButtonClick = viewmodel::createStudyGroupClick,
        onCurrentStudyImageChanged = viewmodel::onImageByteArrayChanged,
    )

    if (uiState.isLoading) {
        WeQuizCircularProgressIndicator()
    }

    if (uiState.isSubmitStudySuccess) {
        LaunchedEffect(Unit) {
            onSubmitStudySuccess()
        }
    }

    uiState.snackBarMessage?.let { message ->
        LaunchedEffect(message) {
            snackBarHostState.showSnackbar(message)
            viewmodel.onSnackBarShown()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudyScreen(
    isEditMode: Boolean,
    defaultStudyImageUri: String?,
    currentStudyImage: ByteArray?,
    titleText: String,
    descriptionText: String,
    groupMemberNumber: String,
    canSubmitStudy: Boolean,
    snackBarHostState: SnackbarHostState,
    onNavigationButtonClick: () -> Unit,
    onTitleTextChange: (String) -> Unit,
    onDescriptionTextChange: (String) -> Unit,
    onMaxUserNumChange: (String) -> Unit,
    onStudyEditButtonClick: () -> Unit,
    onCreationButtonClick: () -> Unit,
    onCurrentStudyImageChanged: (ByteArray) -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CreateStudyTopAppBar(
                isEditMode = isEditMode,
                onNavigationButtonClick = onNavigationButtonClick,
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            WeQuizPhotoPickerAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 70.dp)
                    .aspectRatio(1f)
                    .clip(shape = MaterialTheme.shapes.large),
                imageData = currentStudyImage ?: defaultStudyImageUri,
                onImageDataChanged = onCurrentStudyImageChanged,
            )

            WeQuizValidateTextField(
                label = stringResource(R.string.txt_create_study_title_text_field_label),
                text = titleText,
                onTextChanged = onTitleTextChange,
                placeholder = stringResource(R.string.txt_create_study_title_text_field_placeholder),
                errorMessage = stringResource(R.string.txt_study_group_name_error_message),
                validFun = { it.length <= 20 },
            )

            WeQuizValidateTextField(
                label = stringResource(R.string.txt_create_study_description_label),
                text = descriptionText,
                minLines = 6,
                maxLines = 6,
                onTextChanged = onDescriptionTextChange,
                placeholder = stringResource(R.string.txt_create_study_description_placeholder),
                errorMessage = stringResource(R.string.txt_study_group_description_error_message),
                validFun = { it.length <= 100 },
            )

            WeQuizValidateTextField(
                label = stringResource(R.string.txt_create_study_group_member_number_label),
                text = groupMemberNumber,
                onTextChanged = onMaxUserNumChange,
                placeholder = stringResource(R.string.txt_create_study_group_member_number_placeholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                validFun = ::isValidateNumber,
                errorMessage = stringResource(R.string.txt_create_study_group_number_error_message),
            )

            StudySubmitButton(
                isEditMode = isEditMode,
                onStudyEditButtonClick = onStudyEditButtonClick,
                onStudyCreateButtonClick = onCreationButtonClick,
                canSubmitStudy = canSubmitStudy,
            )
        }
    }
}

fun isValidateNumber(inputNumber: String): Boolean {
    if (inputNumber.isBlank()) return true
    val isValid = inputNumber.matches(Regex("^-?\\d+\$"))
    return isValid && inputNumber.toIntOrNull()?.let { it in 2..50 } == true
}

@Preview(showBackground = true)
@Composable
fun CreateStudyScreenPreview() {
    WeQuizTheme {
        CreateStudyScreen(
            isEditMode = false,
            defaultStudyImageUri = null,
            currentStudyImage = null,
            titleText = "",
            descriptionText = "",
            groupMemberNumber = "",
            canSubmitStudy = false,
            snackBarHostState = remember { SnackbarHostState() },
            onNavigationButtonClick = {},
            onTitleTextChange = {},
            onDescriptionTextChange = {},
            onMaxUserNumChange = {},
            onStudyEditButtonClick = {},
            onCreationButtonClick = {},
            onCurrentStudyImageChanged = {},
        )
    }
}
