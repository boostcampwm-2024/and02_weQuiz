package kr.boostcamp_2024.course.data.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.domain.model.AiQuestion

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AiQuestionResponse(
    val result: ResultDTO,
    val status: StatusDTO,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StatusDTO(
    val code: String,
    val message: String,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ResultDTO(
    val message: MessageDTO,
    val inputLength: Long,
    val outputLength: Long,
    val stopReason: String,
    val seed: Long,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MessageDTO(
    val content: String,
    val role: String,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ContentDTO(
    val answer: String,
    val choices: List<String>,
    val description: String,
    val title: String,
    val explanation: String,
) {
    fun toVO() = AiQuestion(
        answer = answer,
        choices = choices,
        description = description,
        title = title,
        solution = explanation,
    )
}
