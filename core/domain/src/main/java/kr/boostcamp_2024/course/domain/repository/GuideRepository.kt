package kr.boostcamp_2024.course.domain.repository

interface GuideRepository {
    suspend fun getGuideStatus(): Result<Boolean>

    suspend fun setGuideStatus(status: Boolean): Result<Unit>
}
