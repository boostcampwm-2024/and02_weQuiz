package kr.boostcamp_2024.course.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.login.presentation.LoginScreen
import kr.boostcamp_2024.course.login.presentation.SignUpScreen

@Serializable
data object LoginRoute

@Serializable
data object SignUpRoute

// fun NavController.navigationLogin() {
//    navigate(LoginRoute)
// }

fun NavController.navigationSignUp() {
    navigate(SignUpRoute)
}

fun NavGraphBuilder.loginNavGraph(
    onNavigationButtonClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onSignUpButtonClick: () -> Unit,
    onSignUpSuccess: () -> Unit,
) {
    composable<LoginRoute> {
        LoginScreen(
            onLoginSuccess = onLoginSuccess,
            onSignUpButtonClick = onSignUpButtonClick,
        )
    }
    composable<SignUpRoute> {
        SignUpScreen(
            onSignUpSuccess = onSignUpSuccess,
            onNavigationButtonClick = onNavigationButtonClick,
        )
    }
}
