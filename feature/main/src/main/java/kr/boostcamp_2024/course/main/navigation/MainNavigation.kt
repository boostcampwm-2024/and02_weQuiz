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

fun NavController.navigateMain() {
    navigate(MainRoute)
}

fun NavController.navigateNotification() {
    navigate(NotificationRoute)
}

fun NavGraphBuilder.mainNavGraph(
    onNavigationButtonClick: () -> Unit,
    onNotificationButtonClick: () -> Unit,
    onCreateStudyButtonClick: () -> Unit,
    onEditStudyButtonClick: (String) -> Unit,
    onStudyGroupClick: (String) -> Unit,
) {
    composable<MainRoute> {
        MainScreen(
            onNotificationButtonClick = onNotificationButtonClick,
            onCreateStudyButtonClick = onCreateStudyButtonClick,
            onStudyGroupClick = onStudyGroupClick,
            onEditStudyButtonClick = onEditStudyButtonClick,
        )
    }
    composable<NotificationRoute> {
        NotificationScreen(
            onNavigationButtonClick = onNavigationButtonClick,
        )
    }
}
