import kr.boostcamp_2024.course.domain.model.BlankQuestionManager
import org.junit.Before
import org.junit.Test

class BlankQuestionQuizWithAnswerTest {
    private lateinit var blankQuestionWithAnswer: BlankQuestionManager

    @Before
    fun initBlankQuestionWithAnswer() {
        blankQuestionWithAnswer = BlankQuestionManager { }
        blankQuestionWithAnswer.setNewQuestions(
            questionContents = listOf(
                mapOf("type" to "text", "text" to "텍스트1"),
                mapOf("type" to "blank", "text" to "낱말1"),
                mapOf("type" to "text", "text" to "텍스트2"),
                mapOf("type" to "blank", "text" to "낱말2"),
            ),
        )
    }

    @Test
    fun `값이 null인 가장 작은 list index는 2이다`() {
        val targetIdx = blankQuestionWithAnswer.getNullContentMinIndex()
        assert(targetIdx == 1)
    }

    @Test
    fun `낱말 추가`() {
        val targetIdx = blankQuestionWithAnswer.getNullContentMinIndex()

        blankQuestionWithAnswer.addBlankContent(0)
        assert(blankQuestionWithAnswer.contents[targetIdx]?.get("index") == 0)
        assert(blankQuestionWithAnswer.blankWords[0]["isUsed"] == true)
    }

    @Test
    fun `낱말 제거`() {
        val targetIdx = blankQuestionWithAnswer.getNullContentMinIndex()

        blankQuestionWithAnswer.addBlankContent(0)
        blankQuestionWithAnswer.removeBlankContent(targetIdx)

        assert(blankQuestionWithAnswer.contents[targetIdx] == null)
        assert(blankQuestionWithAnswer.blankWords[0]["isUsed"] == false)
    }

    @Test
    fun `정답 가져오기`() {
        blankQuestionWithAnswer.addBlankContent(0)

        val answer = blankQuestionWithAnswer.getAnswer()
        assert(answer.size == 2)
        assert(answer["1"] == null)
    }
}
