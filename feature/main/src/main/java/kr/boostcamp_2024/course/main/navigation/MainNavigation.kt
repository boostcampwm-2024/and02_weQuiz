package kr.boostcamp_2024.course.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.main.presentation.MainScreen
import kr.boostcamp_2024.course.main.presentation.NotificationScreen

@Serializable
data object MainRoute

@Serializable
data object NotificationRoute

fun NavController.navigationMain() {
    navigate(MainRoute)
}

fun NavController.navigationNotification() {
    navigate(NotificationRoute)
}

fun NavGraphBuilder.mainNavGraph(
    onNavigationButtonClick: () -> Unit,
    onCreateStudyButtonClick: () -> Unit,
    onStudyClick: () -> Unit,
) {
    composable<MainRoute> {
        MainScreen(
            onNotificationButtonClick = onNavigationButtonClick,
            onCreateStudyButtonClick = onCreateStudyButtonClick,
            onStudyClick = onStudyClick,
        )
    }
    composable<NotificationRoute> {
        NotificationScreen(
            onNavigationButtonClick = onNavigationButtonClick,
        )
    }
}