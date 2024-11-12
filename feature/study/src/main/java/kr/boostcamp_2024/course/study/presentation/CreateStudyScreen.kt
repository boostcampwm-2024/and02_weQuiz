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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.study.CreateStudyViewModel
import kr.boostcamp_2024.course.study.component.CreateStudyTopAppBar
import kr.boostcamp_2024.course.study.component.DescriptionTextField
import kr.boostcamp_2024.course.study.component.MembersDropDownMenu
import kr.boostcamp_2024.course.study.component.StudyCreationButton
import kr.boostcamp_2024.course.study.component.StudyCreationGuide
import kr.boostcamp_2024.course.study.component.TitleTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudyScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateStudySuccess: () -> Unit,
    viewmodel: CreateStudyViewModel = hiltViewModel<CreateStudyViewModel>()
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    var expanded by remember { mutableStateOf(false) }
    val selectedOption = viewmodel.selectedOption.collectAsState().value

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

                TitleTextField(titleText = uiState.name,
                    onTitleTextChange = viewmodel::onNameChanged,
                    onClearTitleText = { viewmodel.onNameChanged("") })

                DescriptionTextField(descriptionText = uiState.description,
                    onDescriptionTextChange = viewmodel::onDescriptionChanged,
                    onClearDescriptionText = { viewmodel.onDescriptionChanged("") })

                MembersDropDownMenu(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    selectedOption = selectedOption,
                    onDismissRequest = { expanded = false },
                    onOptionSelected = { option -> viewmodel.onOptionSelected(option) },
                )
            }

            StudyCreationButton(onClick = {
                viewmodel.onCreateStudyGroupClick(onCreateStudySuccess)
            })
            uiState.snackBarMessage?.let { message ->
                LaunchedEffect(message) {
                    snackBarHostState.showSnackbar(message)
                    viewmodel.onSnackBarShown() // 스낵바 메시지 초기화
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateStudyScreenPreview() {
    CreateStudyScreen(
        onNavigationButtonClick = {},
        onCreateStudySuccess = {},
    )
}