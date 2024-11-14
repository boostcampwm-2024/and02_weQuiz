package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.UserOmr

interface UserOmrRepository{
    suspend fun submitQuiz(userOmr: UserOmr): Result<String>
}
