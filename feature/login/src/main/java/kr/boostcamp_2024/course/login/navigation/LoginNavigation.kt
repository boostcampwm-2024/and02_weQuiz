package kr.boostcamp_2024.course.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.login.model.UserUiModel
import kr.boostcamp_2024.course.login.navigation.CustomNavType.UserUiModelType
import kr.boostcamp_2024.course.login.presentation.LoginScreen
import kr.boostcamp_2024.course.login.presentation.SignUpScreen
import kotlin.reflect.typeOf

@Serializable
data object LoginRoute

@Serializable
data class SignUpRoute(
    val userUiModel: UserUiModel? = null,
    val userId: String? = null,
)

fun NavController.navigationLogin() {
    navigate(LoginRoute) {
        popUpTo(0)
    }
}

fun NavController.navigationSignUp(
    userUiModel: UserUiModel? = null,
    userId: String? = null,
) {
    navigate(SignUpRoute(userUiModel, userId))
}

fun NavController.navigationSignUp(
    userId: String? = null,
) {
    navigate(SignUpRoute(userId = userId))
}

fun NavGraphBuilder.loginNavGraph(
    onNavigationButtonClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onSignUp: (UserUiModel) -> Unit,
    onSignUpSuccess: () -> Unit,
) {
    composable<LoginRoute> {
        LoginScreen(
            onLoginSuccess = onLoginSuccess,
            onSignUp = onSignUp,
        )
    }

    composable<SignUpRoute>(
        typeMap = mapOf(typeOf<UserUiModel?>() to UserUiModelType),
    ) {
        SignUpScreen(
            onSignUpSuccess = onSignUpSuccess,
            onNavigationButtonClick = onNavigationButtonClick,
        )
    }
}
