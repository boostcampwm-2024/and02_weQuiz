package kr.boostcamp_2024.course.data.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AiQuestionRequest(
    @SerialName("messages") val messages: List<Message> = Message.defaultMessage,
    @SerialName("topP") val topP: Double = 0.8,
    @SerialName("topK") val topK: Int = 0,
    @SerialName("maxTokens") val maxTokens: Int = 1024,
    @SerialName("temperature") val temperature: Double = 0.5,
    @SerialName("repeatPenalty") val repeatPenalty: Double = 5.0,
    @SerialName("stopBefore") val stopBefore: List<String> = emptyList(),
    @SerialName("includeAiFilters") val includeAiFilters: Boolean = false,
    @SerialName("seed") val seed: Long = 0,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Message(
    @SerialName("role") val role: String = "",
    @SerialName("content") val content: String = "",
) {
    companion object {
        val defaultMessage: List<Message> = listOf(
            Message(
                "system",
                "너는 퀴즈 문제를 생성하는 똑똑한 선생님이야. 주어진 주제에 대해 다음의 작업을 수행해줘.\r\n\r\n1. 주제에 맞는 객관식 4지선다 문제를 생성해줘. 문제는 학생들이 이해할 수 있도록 명확하게 작성해줘. \"choice\"에는 무조건 보기 4개만 넣어줘. 여기에 정답은 반드시 하나만 있어야 해\n2. . 객관식 퀴즈를 생성해야하는데 보기는 4개로 하고 이 중에 정답은 1개만 나오도록 구성해야해\n3. 문제의 정답을 \"answer\"에 포함시켜줘\r\n4. 문제에 대한 해설도 포함해줘. 해설은 문제의 정답과 관련된 내용을 충분히 설명하도록 해줘. \r\n5. 결과는 JSON 형식으로 출력해줘.\n6. explanation은 100자 이상 200자 미만으로 작성해. 예시 {인풋} : 네트워크 통신 {아웃풋} : {\"answer\": \"소켓\",\"choices\": [\"배터리\",\"소켓\",\"리모컨\",\"데이터 케이블\"],\"description\": \"네트워크 통신에서 데이터 전송을 위한 연결 지점을 나타내는 것은 무엇인가요?\",\"title\": \"소켓의 역할\",\"explanation\": \"소켓은 네트워크 상에서 데이터를 송수신하기 위해 사용하는 연결의 끝점을 의미합니다. 클라이언트와 서버 간의 통신은 소켓을 통해 이루어지며, 소켓은 IP 주소와 포트 번호로 구성됩니다.\"}",
            ),
        )
    }
}
