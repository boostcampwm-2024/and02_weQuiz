package kr.boostcamp_2024.course.login.model

import kotlinx.serialization.Serializable

@Serializable
data class UserUiModel(
    val id: String,
    val email: String,
    val name: String,
    val profileImageUrl: String?,
)
