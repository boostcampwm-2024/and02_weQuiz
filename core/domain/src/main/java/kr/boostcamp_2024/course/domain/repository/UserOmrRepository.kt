package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.UserOmr
import kr.boostcamp_2024.course.domain.model.UserOmrCreationInfo

interface UserOmrRepository {
    suspend fun getUserOmr(userOmrId: String): Result<UserOmr>

    suspend fun submitQuiz(userOmrCreationInfo: UserOmrCreationInfo): Result<String>

    suspend fun deleteUserOmrs(userOmrIds: List<String>): Result<Unit>
}
