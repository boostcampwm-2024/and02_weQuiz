package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.StudyGroup

data class StudyGroupDTO(
    val name: String? = null,
    @get:PropertyName("study_group_image_url")
    @set:PropertyName("study_group_image_url")
    var studyGroupImageUrl: String? = null,
    val description: String? = null,
    @get:PropertyName("max_user_num")
    @set:PropertyName("max_user_num")
    var maxUserNum: Int? = null,
    @get:PropertyName("owner_id")
    @set:PropertyName("owner_id")
    var ownerId: String? = null,
    val users: List<String>? = null,
    val categories: List<String>? = null,
) {
    fun toVO(groupId: String): StudyGroup = StudyGroup(
        id = groupId,
        name = requireNotNull(name),
        studyGroupImageUrl = studyGroupImageUrl,
        description = description,
        maxUserNum = requireNotNull(maxUserNum),
        ownerId = requireNotNull(ownerId),
        users = requireNotNull(users),
        categories = requireNotNull(categories),
    )
}
