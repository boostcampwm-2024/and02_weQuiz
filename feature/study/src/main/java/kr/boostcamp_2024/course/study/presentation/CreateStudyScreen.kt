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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.study.CreateStudyViewModel
import kr.boostcamp_2024.course.study.component.CreateStudyTopAppBar
import kr.boostcamp_2024.course.study.component.DescriptionTextField
import kr.boostcamp_2024.course.study.component.MembersDropDownMenu
import kr.boostcamp_2024.course.study.component.StudyCreationButton
import kr.boostcamp_2024.course.study.component.StudyCreationGuide
import kr.boostcamp_2024.course.study.component.TitleTextField


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
        onClearTitleText = { viewmodel.onNameChanged("") },
        descriptionText = uiState.description,
        onDescriptionTextChange = viewmodel::onDescriptionChanged,
        onClearDescriptionText = { viewmodel.onDescriptionChanged("") },
        selectedOption = uiState.selectedOption,
        onCreationButtonClick = {
            viewmodel.onCreateStudyGroupClick(onCreateStudySuccess)
        },
        snackBarMessage = uiState.snackBarMessage,
        onNavigationButtonClick = onNavigationButtonClick,
        onOptionSelected = { option -> viewmodel.onOptionSelected(option) },
        onSnackBarShown = { viewmodel.onSnackBarShown() },
        expanded = uiState.expanded,
        onExpandedChange = { expanded -> viewmodel.onExpandedChange(expanded) },
        onDismissRequest = { viewmodel.changeExpandedFalse() }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudyScreen(
    onNavigationButtonClick: () -> Unit,
    titleText: String,
    onTitleTextChange: (String) -> Unit,
    onClearTitleText: () -> Unit,
    descriptionText: String,
    onDescriptionTextChange: (String) -> Unit,
    onClearDescriptionText: () -> Unit,
    selectedOption: String,
    snackBarMessage: String?,
    onCreationButtonClick: () -> Unit,
    onSnackBarShown: () -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onOptionSelected: (String) -> Unit,
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CreateStudyTopAppBar(onNavigationButtonClick = onNavigationButtonClick)
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            StudyCreationGuide()

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TitleTextField(
                    titleText = titleText,
                    onTitleTextChange = onTitleTextChange,
                    onClearTitleText = onClearTitleText,
                )
                DescriptionTextField(
                    descriptionText = descriptionText,
                    onDescriptionTextChange = onDescriptionTextChange,
                    onClearDescriptionText = onClearDescriptionText
                )
                MembersDropDownMenu(
                    expanded = expanded,
                    onExpandedChange = onExpandedChange,
                    selectedOption = selectedOption,
                    onDismissRequest = onDismissRequest,
                    onOptionSelected = onOptionSelected,
                )
            }

            StudyCreationButton(onClick = onCreationButtonClick)
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
            onClearTitleText = {},
            descriptionText = "",
            onDescriptionTextChange = {},
            onClearDescriptionText = {},
            selectedOption = "",
            snackBarMessage = "",
            onCreationButtonClick = {},
            onSnackBarShown = {},
            expanded = false,
            onExpandedChange = {},
            onDismissRequest = {},
            onOptionSelected = {},
        )
    }
}