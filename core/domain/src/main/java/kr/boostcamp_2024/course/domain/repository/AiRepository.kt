package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.AiQuestion

interface AiRepository {
    suspend fun getAiQuestion(question: String): Result<AiQuestion>
}
