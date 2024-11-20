package kr.boostcamp_2024.course.study.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.study.presentation.CreateStudyScreen
import kr.boostcamp_2024.course.study.presentation.DetailStudyScreen

@Serializable
data class CreateStudyRoute(
    val studyGroupId: String? = null,
)

@Serializable
data class StudyRoute(
    val studyGroupId: String,
)

fun NavController.navigateCreateStudy(studyGroupId: String? = null) {
    navigate(CreateStudyRoute(studyGroupId))
}

fun NavController.navigateStudy(studyGroupId: String) {
    navigate(StudyRoute(studyGroupId))
}

fun NavGraphBuilder.studyNavGraph(
    onNavigationButtonClick: () -> Unit,
    onSubmitStudySuccess: () -> Unit,
    onDeleteStudyGroupSuccess: () -> Unit,
    onLeaveStudyGroupSuccess: () -> Unit,
    onEditStudyGroupButtonClick: (String) -> Unit,
    onCreateStudySuccess: () -> Unit,
    onCategoryClick: (String, String) -> Unit,
    onCreateCategoryButtonClick: (String?, String?) -> Unit,
) {
    composable<CreateStudyRoute> {
        CreateStudyScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onSubmitStudySuccess = onSubmitStudySuccess,
        )
    }

    composable<StudyRoute> {
        DetailStudyScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateCategoryButtonClick = onCreateCategoryButtonClick,
            onEditStudyGroupButtonClick = onEditStudyGroupButtonClick,
            onCategoryClick = onCategoryClick,
            onDeleteStudyGroupSuccess = onDeleteStudyGroupSuccess,
            onLeaveStudyGroupSuccess = onLeaveStudyGroupSuccess,
        )
    }
}
