package kr.boostcamp_2024.course.login.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.boostcamp_2024.course.login.model.UserUiModel
import kr.boostcamp_2024.course.login.navigation.CustomNavType.UserUiModelType
import kr.boostcamp_2024.course.login.presentation.LoginScreen
import kr.boostcamp_2024.course.login.presentation.SignUpScreen

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
    val route = buildString {
        append("signUpRoute?")
        userUiModel?.let { append("userUiModel=${Uri.encode(Json.encodeToString(it))}&") }
        userId?.let { append("userId=$it") }
    }
    navigate(route)
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

    composable(
        route = "signUpRoute?userUiModel={userUiModel?}&userId={userId?}",
        arguments = listOf(
            navArgument("userUiModel") {
                type = UserUiModelType
                nullable = true
            },
            navArgument("userId") {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        SignUpScreen(
            onSignUpSuccess = onSignUpSuccess,
            onNavigationButtonClick = onNavigationButtonClick,
        )
    }
}
