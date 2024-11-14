package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.User

data class UserDTO(
    val email: String? = null,
    val name: String? = null,
    @get:PropertyName("profile_url")
    @set:PropertyName("profile_url")
    var profileUrl: String? = null,
    @get:PropertyName("study_groups")
    @set:PropertyName("study_groups")
    var studyGroups: List<String>? = null,
) {
    fun toVO(userId: String): User = User(
        id = userId,
        email = requireNotNull(email),
        name = requireNotNull(name),
        profileUrl = profileUrl,
        studyGroups = requireNotNull(studyGroups),
    )
}
