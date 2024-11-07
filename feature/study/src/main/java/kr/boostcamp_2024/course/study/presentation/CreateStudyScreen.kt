package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.component.CreateStudyTopAppBar
import kr.boostcamp_2024.course.study.component.DescriptionTextField
import kr.boostcamp_2024.course.study.component.MembersDropDownMenu
import kr.boostcamp_2024.course.study.component.StudyCreationButton
import kr.boostcamp_2024.course.study.component.StudyCreationGuide
import kr.boostcamp_2024.course.study.component.TitleTextField


@ExperimentalMaterial3Api
@Composable
fun CreateStudyScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateStudySuccess: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    Scaffold(topBar = {
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

                TitleTextField(titleText = titleText,
                    onTitleTextChange = { titleText = it },
                    onClearTitleText = { titleText = "" })

                DescriptionTextField(descriptionText = descriptionText,
                    onDescriptionTextChange = { descriptionText = it },
                    onClearDescriptionText = { descriptionText = "" })

                MembersDropDownMenu()

            }
            StudyCreationButton(onCreateStudySuccess = onCreateStudySuccess)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CreateStudyScreenPreview() {
    CreateStudyScreen(
        onNavigationButtonClick = {},
        onCreateStudySuccess = {},
    )
}