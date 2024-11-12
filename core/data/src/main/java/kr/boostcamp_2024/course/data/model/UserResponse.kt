package kr.boostcamp_2024.course.data.model

import kr.boostcamp_2024.course.domain.model.User

data class UserResponse(
    val email: String? = null,
    val name: String? = null,
    val profileUrl: String? = null,
    val studyGroups: List<String>? = null,
) {
    fun toVO(): User = User(
        email = requireNotNull(email),
        name = requireNotNull(name),
        profileUrl = profileUrl,
        studyGroups = requireNotNull(studyGroups),
    )
}
