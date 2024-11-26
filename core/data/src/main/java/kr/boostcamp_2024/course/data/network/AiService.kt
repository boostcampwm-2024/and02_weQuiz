package kr.boostcamp_2024.course.data.network

import kr.boostcamp_2024.course.data.BuildConfig
import kr.boostcamp_2024.course.data.model.AiQuestionRequest
import kr.boostcamp_2024.course.data.model.AiQuestionResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AiService {
    @POST(BuildConfig.POST_URL)
    @Headers(
        "X-NCP-CLOVASTUDIO-API-KEY: ${BuildConfig.X_NCP_CLOVASTUDIO_API_KEY}",
        "X-NCP-APIGW-API-KEY: ${BuildConfig.X_NCP_APIGW_API_KEY}",
        "X-NCP-CLOVASTUDIO-REQUEST-ID: ${BuildConfig.X_NCP_CLOVASTUDIO_REQUEST_ID}",
        "Content-Type: application/json",
    )
    suspend fun getAiQuestion(
        @Body question: AiQuestionRequest,
    ): AiQuestionResponse
}
