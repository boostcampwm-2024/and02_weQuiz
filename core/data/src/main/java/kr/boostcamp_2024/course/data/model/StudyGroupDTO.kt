package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName

class StudyGroupDTO(
    val categories: List<String>? = null,
    val description: String? = null,
    @get:PropertyName("max_user_num")
    @set:PropertyName("max_user_num")
    var maxUserNum: Int? = null,
    val name: String? = null,
    @get:PropertyName("owner_id")
    @set:PropertyName("owner_id")
    var ownerId: String? = null,
    @get:PropertyName("study_group_image_url")
    @set:PropertyName("study_group_image_url")
    var studyGroupImageUrl: String? = null,
    val users: List<String>? = null,
)
