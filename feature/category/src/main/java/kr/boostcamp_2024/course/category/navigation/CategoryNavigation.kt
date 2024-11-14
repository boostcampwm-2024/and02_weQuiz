package kr.boostcamp_2024.course.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.category.presentation.CategoryScreen
import kr.boostcamp_2024.course.category.presentation.CreateCategoryScreen

@Serializable
data class CategoryRoute(
    val categoryId: String,
)

@Serializable
data object CreateCategoryRoute

fun NavController.navigateCategory(categoryId: String) {
    navigate(CategoryRoute(categoryId))
}

fun NavController.navigateCreateCategory() {
    navigate(CreateCategoryRoute)
}

fun NavGraphBuilder.categoryNavGraph(
    onNavigationButtonClick: () -> Unit,
    onCreateQuizButtonClick: () -> Unit,
    onQuizClick: (String, String) -> Unit,
    onCreateCategorySuccess: () -> Unit,
) {
    composable<CategoryRoute> {
        CategoryScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateQuizButtonClick = onCreateQuizButtonClick,
            onQuizClick = onQuizClick,
        )
    }
    composable<CreateCategoryRoute> {
        CreateCategoryScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateCategorySuccess = onCreateCategorySuccess,
        )
    }
}
