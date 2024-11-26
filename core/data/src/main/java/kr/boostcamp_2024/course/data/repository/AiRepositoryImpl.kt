package kr.boostcamp_2024.course.data.repository

import kotlinx.serialization.json.Json
import kr.boostcamp_2024.course.data.model.AiQuestionRequest
import kr.boostcamp_2024.course.data.model.ContentDTO
import kr.boostcamp_2024.course.data.network.AiService
import kr.boostcamp_2024.course.domain.model.AiQuestion
import kr.boostcamp_2024.course.domain.repository.AiRepository
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val aiService: AiService,
) : AiRepository {
    override suspend fun getAiQuestion(topic: String): Result<AiQuestion> =
        runCatching {
            val response = aiService.getAiQuestion(AiQuestionRequest())
            val content: ContentDTO = Json.decodeFromString(response.result.message.content)
            content.toVO()
        }
}
