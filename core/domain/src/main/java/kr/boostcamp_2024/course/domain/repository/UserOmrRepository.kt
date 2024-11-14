package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.UserOmr

interface UserOmrRepository {
    suspend fun getUserOmr(userOmrId: String): Result<UserOmr>
}
