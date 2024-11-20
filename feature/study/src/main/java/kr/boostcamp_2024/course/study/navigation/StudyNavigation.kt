package kr.boostcamp_2024.course.study.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.study.presentation.CreateStudyScreen
import kr.boostcamp_2024.course.study.presentation.DetailStudyScreen

@Serializable
data object CreateStudyRoute

@Serializable
data class StudyRoute(
    val studyGroupId: String,
)

fun NavController.navigateCreateStudy() {
    navigate(CreateStudyRoute)
}

fun NavController.navigateStudy(studyGroupId: String) {
    navigate(StudyRoute(studyGroupId))
}

fun NavGraphBuilder.studyNavGraph(
    onNavigationButtonClick: () -> Unit,
    onCreateStudySuccess: () -> Unit,
    onCategoryClick: (String, String) -> Unit,
    onCreateCategoryButtonClick: (String?, String?) -> Unit,
) {
    composable<CreateStudyRoute> {
        CreateStudyScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateStudySuccess = onCreateStudySuccess,
        )
    }

    composable<StudyRoute> {
        DetailStudyScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateCategoryButtonClick = onCreateCategoryButtonClick,
            onCategoryClick = onCategoryClick,
        )
    }
}
