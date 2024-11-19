package kr.boostcamp_2024.course.login.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.boostcamp_2024.course.login.model.UserUiModel

object CustomNavType {
    val UserUiModelType = object : NavType<UserUiModel>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): UserUiModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): UserUiModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: UserUiModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: UserUiModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}
