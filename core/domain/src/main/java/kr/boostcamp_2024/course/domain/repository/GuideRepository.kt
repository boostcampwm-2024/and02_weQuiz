package kr.boostcamp_2024.course.domain.repository

interface GuideRepository {
    suspend fun getGuideStatus(): Boolean

    suspend fun setGuideStatus(status: Boolean)
}
