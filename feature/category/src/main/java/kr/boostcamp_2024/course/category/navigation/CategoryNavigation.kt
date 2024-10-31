package kr.boostcamp_2024.course.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.category.presentation.CategoryScreen
import kr.boostcamp_2024.course.category.presentation.CreateCategoryScreen

@Serializable
data object CategoryRoute

@Serializable
data object CreateCategoryRoute

fun NavController.navigationCategory() {
    navigate(CategoryRoute)
}

fun NavController.navigationCreateCategory() {
    navigate(CreateCategoryRoute)
}

fun NavGraphBuilder.categoryNavGraph(
    onNavigationButtonClick: () -> Unit,
    onCreateQuestionButtonClick: () -> Unit,
    onQuizClick: () -> Unit,
    onCreateCategorySuccess: () -> Unit,
) {
    composable<CategoryRoute> {
        CategoryScreen(
            onNavigationButtonClick = onNavigationButtonClick,
            onCreateQuestionButtonClick = onCreateQuestionButtonClick,
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

