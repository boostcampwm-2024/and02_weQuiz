package kr.boostcamp_2024.course.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.login.presentation.LoginScreen

@Serializable
data object LoginRoute

//fun NavController.navigationLogin() {
//    navigate(LoginRoute)
//}

fun NavGraphBuilder.loginNavGraph(
    onLoginSuccess: () -> Unit,
) {
    composable<LoginRoute> {
        LoginScreen(
            onLoginSuccess = onLoginSuccess,
        )
    }
}