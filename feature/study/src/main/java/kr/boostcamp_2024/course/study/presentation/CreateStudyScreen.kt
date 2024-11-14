package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.study.CreateStudyViewModel
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CreateStudyTopAppBar
import kr.boostcamp_2024.course.study.component.MembersDropDownMenu
import kr.boostcamp_2024.course.study.component.StudyCreationButton
import kr.boostcamp_2024.course.study.component.StudyCreationGuide

@Composable
fun CreateStudyScreen(
    viewmodel: CreateStudyViewModel = hiltViewModel<CreateStudyViewModel>(),
    onNavigationButtonClick: () -> Unit,
    onCreateStudySuccess: () -> Unit,
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    CreateStudyScreen(
        titleText = uiState.name,
        onTitleTextChange = viewmodel::onNameChanged,
        descriptionText = uiState.description,
        onDescriptionTextChange = viewmodel::onDescriptionChanged,
        onCreationButtonClick = viewmodel::createStudyGroupClick,
        snackBarMessage = uiState.snackBarMessage,
        onNavigationButtonClick = onNavigationButtonClick,
        onSnackBarShown = viewmodel::onSnackBarShown,
        isCreateStudySuccess = uiState.isCreateStudySuccess,
        onCreateStudySuccess = onCreateStudySuccess,
        onOptionSelected = viewmodel::onOptionSelected,
        isCreateStudyButtonEnabled = uiState.isCreateStudyButtonEnabled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudyScreen(
    onNavigationButtonClick: () -> Unit,
    titleText: String,
    onTitleTextChange: (String) -> Unit,
    descriptionText: String,
    onDescriptionTextChange: (String) -> Unit,
    snackBarMessage: String?,
    onCreationButtonClick: () -> Unit,
    onSnackBarShown: () -> Unit,
    isCreateStudySuccess: Boolean,
    onCreateStudySuccess: () -> Unit,
    onOptionSelected: (Int) -> Unit,
    isCreateStudyButtonEnabled: Boolean,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    if (isCreateStudySuccess) {
        onCreateStudySuccess()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CreateStudyTopAppBar(onNavigationButtonClick = onNavigationButtonClick)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
        ) {
            StudyCreationGuide()

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

                MembersDropDownMenu(onOptionSelected = onOptionSelected)
            }

            StudyCreationButton(
                onStudyCreationButtonClick = onCreationButtonClick,
                isCreateStudyButtonEnabled = isCreateStudyButtonEnabled,
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
            snackBarMessage = "",
            onCreationButtonClick = {},
            onSnackBarShown = {},
            onOptionSelected = {},
            isCreateStudySuccess = false,
            onCreateStudySuccess = {},
            isCreateStudyButtonEnabled = false,
        )
    }
}
