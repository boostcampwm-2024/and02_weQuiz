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
                "너는 학교현장에서 아이들을 가르치는 선생님이야. 주어진 주제에 대해 다음의 작업을 수행해줘.\n" +
                    "\n" +
                    "1.  주제에 맞는 객관식 4지선다 문제를 생성해줘. 문제는 학생들이 이해할 수 있도록 명확하게 작성해줘. \"choice\"에는 무조건 보기 4개만 넣어줘. 여기에 정답은 반드시 하나만 있어야 해\n" +
                    "2. . 객관식 퀴즈를 생성해야하는데 보기는 4개로 하고 이 중에 정답은 1개만 나오도록 구성해야해\n" +
                    "3. 문제의 정답을 \"answer\"에 포함시켜줘\n" +
                    "4. 문제에 대한 해설도 포함해줘. 해설은 문제의 정답과 관련된 내용을 충분히 설명하도록 해줘.\n" +
                    "5. 결과는 JSON 형식으로 출력해줘.\n" +
                    "6. explanation은 100자 이상 200자 미만으로 작성해.\n" +
                    "7. 인풋으로 \"언어, 주제\"를 입력하면 아웃풋은  무조건 해당 언어로 주제에 대해서 설명해줘.\n" +
                    "\n" +
                    "예시\n" +
                    "{인풋} : \n" +
                    "zh, 网络通信\n" +
                    "\n" +
                    "{아웃풋} :\n" +
                    " {\n" +
                    "\"answer\": \"遥控器\",\n" +
                    "\"choices\": [\n" +
                    "\"电池\",\n" +
                    "\"灯头\",\n" +
                    "\"遥控器\",\n" +
                    "\"数据线\"\n" +
                    "],\n" +
                    "\"description\": \" 在网络通信中，代表数据传输的连接点是什么？\",\n" +
                    "\"title\": \"插座的作用\",\n" +
                    "\"explanation\": \" 套接字是指用于在网络上发送和接收数据的连接的端点。 客户端与服务器之间的通信是通过套接字实现的，套接字由IP地址和端口号组成。\"\n" +
                    "}",
            ),
        )
    }
}
