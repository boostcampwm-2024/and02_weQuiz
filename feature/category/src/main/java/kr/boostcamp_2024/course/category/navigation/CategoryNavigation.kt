package kr.boostcamp_2024.course.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.category.presentation.CategoryScreen
import kr.boostcamp_2024.course.category.presentation.CreateCategoryScreen

@Serializable
data class CategoryRoute(
    val studyGroupId: String,
    val categoryId: String,
)

@Serializable
data class CreateCategoryRoute(
    val studyGroupId: String?,
    val categoryId: String?,
)

fun NavController.navigateCategory(studyGroupId: String, categoryId: String) {
    navigate(CategoryRoute(studyGroupId,categoryId))
}

fun NavController.navigateCreateCategory(
    studyGroupId: String?, categoryId: String?
) {
    navigate(CreateCategoryRoute(studyGroupId,categoryId))
}

fun NavGraphBuilder.categoryNavGraph(
    onNavigationButtonClick: () -> Unit,
    onCreateQuizButtonClick: (String) -> Unit,
    onQuizClick: (String, String) -> Unit,
    onCreateCategorySuccess: () -> Unit,
    onCreateCategoryButtonClick: (String?, String?) -> Unit,
) {
    composable<CategoryRoute> {
        CategoryScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateQuizButtonClick = onCreateQuizButtonClick,
            onQuizClick = onQuizClick,
            onCreateCategoryButtonClick = onCreateCategoryButtonClick,
        )
    }
    composable<CreateCategoryRoute> {
        CreateCategoryScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateCategorySuccess = onCreateCategorySuccess,
        )
    }
}
