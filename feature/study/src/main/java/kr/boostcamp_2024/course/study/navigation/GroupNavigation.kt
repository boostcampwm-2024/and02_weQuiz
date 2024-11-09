package kr.boostcamp_2024.course.study.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.study.presentation.CreateGroupScreen

@Serializable
data object CreateGroupRoute

fun NavController.navigateCreateGroup() {
    navigate(CreateGroupRoute)
}

fun NavGraphBuilder.groupNavGraph(
    onDismissButtonClick: () -> Unit, onConfirmButtonClick: () -> Unit
) {
    composable<CreateGroupRoute> {
        CreateGroupScreen(
            onDismissButtonClick = onDismissButtonClick,
            onConfirmButtonClick = onConfirmButtonClick,
        )
    }
}
