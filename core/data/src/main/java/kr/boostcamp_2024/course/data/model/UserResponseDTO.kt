package kr.boostcamp_2024.course.data.model

import kr.boostcamp_2024.course.domain.model.UserResponseVO

data class UserResponseDTO(
    val email: String? = null,
    val name: String? = null,
    val profileUrl: String? = null,
    val studyGroups: List<String>? = null
) {
    fun toVO(): UserResponseVO = UserResponseVO(
        email = email ?: "unknown",
        name = name ?: "unknown",
        profileUrl = profileUrl,
        studyGroups = studyGroups ?: throw Exception("Study groups not found")
    )
}