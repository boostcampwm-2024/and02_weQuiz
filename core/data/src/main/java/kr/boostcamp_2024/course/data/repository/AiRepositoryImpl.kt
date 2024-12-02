package kr.boostcamp_2024.course.data.repository

import kotlinx.serialization.json.Json
import kr.boostcamp_2024.course.data.model.AiQuestionRequest
import kr.boostcamp_2024.course.data.model.ContentDTO
import kr.boostcamp_2024.course.data.model.Message
import kr.boostcamp_2024.course.data.network.AiService
import kr.boostcamp_2024.course.domain.model.AiQuestion
import kr.boostcamp_2024.course.domain.repository.AiRepository
import java.util.Locale
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val aiService: AiService,
) : AiRepository {
    override suspend fun getAiQuestion(question: String): Result<AiQuestion> =
        runCatching {
            val language = when (Locale.getDefault().language) {
                "ko" -> "ko"
                "zh" -> "zh"
                else -> "en"
            }
            val newMessage = Message("user", "$language, $question")
            val response =
                aiService.getAiQuestion(AiQuestionRequest(messages = Message.defaultMessage + newMessage, maxTokens = 512))
            val content: ContentDTO = Json.decodeFromString(response.result.message.content)
            content.toVO()
        }
}
