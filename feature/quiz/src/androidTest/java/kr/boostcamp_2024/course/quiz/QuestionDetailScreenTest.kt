package kr.boostcamp_2024.course.quiz

import android.content.Context
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.quiz.presentation.question.QuestionDetailScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuestionDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context
    private lateinit var baseChoiceQuestion: ChoiceQuestion
    private lateinit var baseBlankQuestion: BlankQuestion
    private val baseChoiceList = listOf("객관식 1", "객관식 2", "객관식 3", "객관식 4")
    private val baseBlankContent = listOf(
        mapOf("text" to "빈칸 요소 컨텐트", "type" to "blank"),
        mapOf("text" to "텍스트 요소 컨텐트", "type" to "text"),
    )

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        baseChoiceQuestion = ChoiceQuestion(
            id = "2",
            "문제 제목",
            description = "문제 설명",
            solution = "문제 해설",
            answer = 0,
            choices = baseChoiceList,
            userAnswers = listOf(0, 1, 2, 3, 4),
        )
        baseBlankQuestion = BlankQuestion(
            id = "1",
            title = "문제 제목",
            questionContent = baseBlankContent,
            solution = "문제 해설",
            userAnswers = listOf("userId1", "userId2"),
        )
    }

    fun setQuestionDetailScreen(question: Question) {
        composeTestRule.setContent {
            QuestionDetailScreen(
                question = question,
                onNavigationButtonClick = {},
            )
        }
    }

    // 일반 문제 test
    @Test
    fun 일반_문제_해설에서_4개_객관식_문항을_모두_확인할_수_있다() {
        composeTestRule.setContent {
            QuestionDetailScreen(
                question = baseChoiceQuestion,
                onNavigationButtonClick = {},
            )
        }

        baseChoiceList.forEach { choiceText ->
            composeTestRule.onNodeWithText(choiceText).assertExists()
        }
    }

    @Test
    fun 일반_문제_해설에서_결과_통계를_확인할_수_있다() {
        composeTestRule.setContent {
            QuestionDetailScreen(
                question = baseChoiceQuestion,
                onNavigationButtonClick = {},
            )
        }

        val quizResultFabContentDes = context.getString(R.string.fab_quiz_result)
        composeTestRule.onNodeWithContentDescription(quizResultFabContentDes)
            .assertExists()
    }

    @Test
    fun 일반_문제의_해설이_없을_경우_해설_부분을_볼_수_없다() {
        composeTestRule.setContent {
            QuestionDetailScreen(
                question = baseChoiceQuestion.copy(solution = null),
                onNavigationButtonClick = {},
            )
        }

        val solutionText = context.getString(R.string.txt_question_detail_solution)
        composeTestRule.onNodeWithContentDescription(solutionText)
            .assertDoesNotExist()
    }

    // 빈칸 문제 test
    @Test
    fun 빈칸_문제_해설에서_빈칸_및_텍스트_요소들을_확인할_수_있다() {
        composeTestRule.setContent {
            QuestionDetailScreen(
                question = baseBlankQuestion,
                onNavigationButtonClick = {},
            )
        }

        baseBlankContent.forEach { content ->
            val itemText = content["text"]
            val itemContentDes = if (content["type"] == "blank") {
                context.getString(R.string.des_blank_item, itemText)
            } else {
                context.getString(R.string.des_text_item, itemText)
            }

            composeTestRule.onNodeWithContentDescription(itemContentDes)
                .assertExists()
                .assertIsNotEnabled()
        }
    }

    @Test
    fun 빈칸_문제_해설에서_빈칸_및_텍스트_요소들을_클릭할_수_없다() {
        composeTestRule.setContent {
            QuestionDetailScreen(
                question = baseBlankQuestion,
                onNavigationButtonClick = {},
            )
        }

        baseBlankContent.forEach { content ->
            val itemText = content["text"]
            val itemContentDes = if (content["type"] == "blank") {
                context.getString(R.string.des_blank_item, itemText)
            } else {
                context.getString(R.string.des_text_item, itemText)
            }

            composeTestRule.onNodeWithContentDescription(itemContentDes)
                .assertIsNotEnabled()
        }
    }

    @Test
    fun 빈칸_문제_해설에서_결과_통계를_확인할_수_없다() {
        composeTestRule.setContent {
            QuestionDetailScreen(
                question = baseBlankQuestion,
                onNavigationButtonClick = {},
            )
        }

        val quizResultFabContentDes = context.getString(R.string.fab_quiz_result)
        composeTestRule.onNodeWithContentDescription(quizResultFabContentDes)
            .assertDoesNotExist()
    }

    @Test
    fun 빈칸_문제의_해설이_없을_경우_해설_부분을_볼_수_없다() {
        composeTestRule.setContent {
            QuestionDetailScreen(
                question = baseBlankQuestion.copy(solution = null),
                onNavigationButtonClick = {},
            )
        }

        val solutionText = context.getString(R.string.txt_question_detail_solution)
        composeTestRule.onNodeWithContentDescription(solutionText)
            .assertDoesNotExist()
    }
}
