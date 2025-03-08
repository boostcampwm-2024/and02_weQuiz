package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.UserOmr
import kr.boostcamp_2024.course.domain.model.UserOmrCreationInfo

interface UserOmrRepository {
    suspend fun getUserOmr(userOmrId: String): UserOmr

    suspend fun submitQuiz(userOmrCreationInfo: UserOmrCreationInfo): String

    suspend fun deleteUserOmr(quizId: String)

    suspend fun deleteUserOmrs(userOmrIds: List<String>)
}
