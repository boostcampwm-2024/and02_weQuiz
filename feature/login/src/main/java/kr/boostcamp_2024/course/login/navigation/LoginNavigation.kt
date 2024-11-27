package kr.boostcamp_2024.course.login.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.boostcamp_2024.course.login.model.UserUiModel
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

val UserUiModelType = object : NavType<UserUiModel?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): UserUiModel? =
        bundle.getString(key)?.let(Json::decodeFromString)

    override fun parseValue(value: String): UserUiModel? =
        if (value == "null") null else Json.decodeFromString(value)

    override fun serializeAsValue(value: UserUiModel?): String =
        if (value == null) "null" else Json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: UserUiModel?) {
        bundle.putString(key, if (value == null) "null" else Json.encodeToString(value))
    }
}
