package kr.boostcamp_2024.course.study.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizValidateTextField
import kr.boostcamp_2024.course.study.CreateStudyViewModel
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CreateStudyTopAppBar
import kr.boostcamp_2024.course.study.component.StudySubmitButton

@Composable
fun CreateStudyScreen(
    viewmodel: CreateStudyViewModel = hiltViewModel<CreateStudyViewModel>(),
    onNavigationButtonClick: () -> Unit,
    onCreateStudySuccess: () -> Unit,
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    CreateStudyScreen(
        isEditMode = uiState.isEditMode,
        titleText = uiState.name,
        onTitleTextChange = viewmodel::onNameChanged,
        descriptionText = uiState.description,
        onDescriptionTextChange = viewmodel::onDescriptionChanged,
        groupMemberNumber = uiState.maxUserNum,
        onGroupMemberNumberChange = viewmodel::onGroupMemberNumberChanged,
        onCreationButtonClick = viewmodel::createStudyGroupClick,
        snackBarMessage = uiState.snackBarMessage,
        onNavigationButtonClick = onNavigationButtonClick,
        onSnackBarShown = viewmodel::onSnackBarShown,
        isCreateStudySuccess = uiState.isCreateStudySuccess,
        onCreateStudySuccess = onCreateStudySuccess,
        canSubmitStudy = uiState.canSubmitStudy,
        onStudyImgUriChanged = { },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudyScreen(
    isEditMode: Boolean,
    onNavigationButtonClick: () -> Unit,
    titleText: String,
    onTitleTextChange: (String) -> Unit,
    descriptionText: String,
    onDescriptionTextChange: (String) -> Unit,
    groupMemberNumber: String,
    onGroupMemberNumberChange: (String) -> Unit,
    snackBarMessage: String?,
    onCreationButtonClick: () -> Unit,
    onSnackBarShown: () -> Unit,
    isCreateStudySuccess: Boolean,
    onCreateStudySuccess: () -> Unit,
    canSubmitStudy: Boolean,
    onStudyImgUriChanged: (String) -> Unit,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { imageUri ->
        },
    )

    if (isCreateStudySuccess) {
        onCreateStudySuccess()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CreateStudyTopAppBar(
                isEditMode = isEditMode,
                onNavigationButtonClick = onNavigationButtonClick,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .clip(shape = RoundedCornerShape(18.dp))
                    .clickable(enabled = true) {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                painter = painterResource(R.drawable.img_photo_picker),
                contentDescription = "스터디 배경 이미지",
                contentScale = ContentScale.FillWidth,
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                WeQuizTextField(
                    label = stringResource(R.string.txt_create_study_title_text_field_label),
                    text = titleText,
                    onTextChanged = onTitleTextChange,
                    placeholder = stringResource(R.string.txt_create_study_title_text_field_placeholder),
                )

                WeQuizTextField(
                    label = stringResource(R.string.txt_create_study_description_label),
                    text = descriptionText,
                    minLines = 6,
                    maxLines = 6,
                    onTextChanged = onDescriptionTextChange,
                    placeholder = stringResource(R.string.txt_create_study_description_placeholder),
                )

                WeQuizValidateTextField(
                    label = stringResource(R.string.txt_create_study_group_member_number_label),
                    text = groupMemberNumber,
                    onTextChanged = onGroupMemberNumberChange,
                    placeholder = stringResource(R.string.txt_create_study_group_member_number_placeholder),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    validFun = ::isValidateNumber,
                    errorMessage = stringResource(R.string.txt_create_study_group_number_error_message),
                )
            }

            StudySubmitButton(
                isEditMode = isEditMode,
                onStudyEditButtonClick = onCreationButtonClick,
                onStudyCreateButtonClick = onCreationButtonClick,
                canSubmitStudy = canSubmitStudy,
            )

            snackBarMessage?.let { message ->
                LaunchedEffect(message) {
                    snackBarHostState.showSnackbar(message)
                    onSnackBarShown()
                }
            }
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
            onNavigationButtonClick = {},
            titleText = "",
            onTitleTextChange = {},
            descriptionText = "",
            onDescriptionTextChange = {},
            groupMemberNumber = "",
            onGroupMemberNumberChange = {},
            snackBarMessage = "",
            onCreationButtonClick = {},
            onSnackBarShown = {},
            isCreateStudySuccess = false,
            onCreateStudySuccess = {},
            canSubmitStudy = false,
            onStudyImgUriChanged = {},
            isEditMode = false,
        )
    }
}
