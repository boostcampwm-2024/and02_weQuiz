package kr.boostcamp_2024.course.data.network

import kr.boostcamp_2024.course.data.model.AiQuestionRequest
import kr.boostcamp_2024.course.data.model.AiQuestionResponse
import retrofit2.http.Body
import retrofit2.http.POST

const val POST_URL = "/testapp/v1/chat-completions/HCX-003"

interface AiService {
    @POST(POST_URL)
    suspend fun getAiQuestion(
        @Body question: AiQuestionRequest,
    ): AiQuestionResponse
}
