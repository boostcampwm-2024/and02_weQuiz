package kr.boostcamp_2024.course.study.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.study.presentation.CreateStudyScreen
import kr.boostcamp_2024.course.study.presentation.StudyScreen

@Serializable
data object CreateStudyRoute

@Serializable
data object StudyRoute

fun NavController.navigateCreateStudy() {
    navigate(CreateStudyRoute)
}

fun NavController.navigateStudy() {
    navigate(StudyRoute)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.studyNavGraph(
    onNavigationButtonClick: () -> Unit,
    onCreateStudySuccess: () -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    onCategoryClick: () -> Unit,
) {
    composable<CreateStudyRoute> {
        CreateStudyScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateStudySuccess = onCreateStudySuccess,
        )
    }

    composable<StudyRoute> {
        StudyScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateCategoryButtonClick = onCreateCategoryButtonClick,
            onCategoryClick = onCategoryClick,
        )
    }
}
