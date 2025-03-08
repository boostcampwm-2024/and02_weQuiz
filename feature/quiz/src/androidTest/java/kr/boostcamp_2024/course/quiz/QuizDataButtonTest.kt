package kr.boostcamp_2024.course.quiz

import android.content.Context
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.quiz.component.QuizDataButton
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class QuizDataButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var baseRealTimeQuiz: RealTimeQuiz
    private lateinit var baseQuiz: Quiz
    private lateinit var context: Context
    private val ownerId = "0"
    private val playerId = "1"

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        baseRealTimeQuiz = RealTimeQuiz(
            id = "1",
            title = "퀴즈 제목",
            description = null,
            questions = listOf("quiz1", "quiz2"),
            userOmrs = listOf(),
            currentQuestion = 0,
            ownerId = ownerId,
            isStarted = false,
            isFinished = false,
            waitingUsers = listOf(),
            quizImageUrl = null,
        )
        baseQuiz = Quiz(
            id = "1",
            title = "퀴즈 제목",
            description = null,
            startTime = "2021-09-01",
            solveTime = 10,
            questions = listOf("quiz1", "quiz2"),
            userOmrs = listOf(),
            quizImageUrl = null,
        )
    }

    private fun setContentAndAssert(
        quiz: BaseQuiz,
        currentUserId: String,
        buttonText: String,
        assertEnabled: Boolean,
    ) {
        composeTestRule.setContent {
            QuizDataButton(
                quiz = quiz,
                currentUserId = currentUserId,
                onCreateQuestionButtonClick = {},
                onStartQuizButtonClick = {},
                onStartRealTimeQuizButtonClick = {},
                onWaitingRealTimeQuizButtonClick = {},
            )
        }

        val node = composeTestRule.onNode(hasText(buttonText))
        if (assertEnabled) {
            node.assertIsEnabled()
        } else {
            node.assertIsNotEnabled()
        }
    }

    @Test
    fun 실시간_퀴즈_시작_전_참여자는_대기를_하지_않을_경우_퀴즈를_생성할_수_있다() {
        val buttonText = context.resources.getString(R.string.txt_open_create_question)
        setContentAndAssert(baseRealTimeQuiz, playerId, buttonText, true)
    }

    @Test
    fun 실시간_퀴즈_시작_전_참여자는_대기를_할_경우_퀴즈를_생성할_수_없다() {
        val buttonText = context.resources.getString(R.string.txt_open_create_question)
        val quiz = baseRealTimeQuiz.copy(ownerId = ownerId, waitingUsers = listOf(playerId))
        setContentAndAssert(quiz, playerId, buttonText, false)
    }

    @Test
    fun 문제가_없을_경우_실시간_퀴즈를_시작할_수_없다() {
        val quiz = baseRealTimeQuiz.copy(questions = listOf())
        val buttonText = context.resources.getString(R.string.txt_quiz_question_count_zero)
        setContentAndAssert(quiz, playerId, buttonText, false)
    }

    @Test
    fun 실시간_퀴즈_시작_전_대기가_있을_경우_관리자는_퀴즈를_시작할_수_있다() {
        val quiz = baseRealTimeQuiz.copy(waitingUsers = listOf(playerId))
        val buttonText = context.resources.getString(R.string.txt_real_time_quiz_owner, quiz.waitingUsers.size)
        setContentAndAssert(quiz, ownerId, buttonText, true)
    }

    @Test
    fun 실시간_퀴즈_시작_전_대기가_없을_경우_관리자는_퀴즈를_시작할_수_없다() {
        val buttonText = context.resources.getString(R.string.txt_real_time_quiz_owner, baseRealTimeQuiz.waitingUsers.size)
        setContentAndAssert(baseRealTimeQuiz, ownerId, buttonText, false)
    }

    @Test
    fun 실시간_퀴즈_시작_전_참여자는_퀴즈_참여_대기를_할_수_있다() {
        val buttonText = context.resources.getString(R.string.txt_real_time_quiz_wait_false)
        setContentAndAssert(baseRealTimeQuiz, playerId, buttonText, true)
    }

    @Test
    fun 실시간_퀴즈_진행_중에는_퀴즈에_참여할_수_없다() {
        val quiz = baseRealTimeQuiz.copy(isStarted = true)
        val buttonText = context.resources.getString(R.string.txt_real_time_quiz_progressing)
        setContentAndAssert(quiz, playerId, buttonText, false)
    }

    @Test
    fun 실시간_퀴즈_종료_후에는_퀴즈가_닫힌다() {
        val quiz = baseRealTimeQuiz.copy(
            isStarted = true,
            isFinished = true,
        )
        val buttonText = context.resources.getString(R.string.txt_real_time_quiz_finished)
        setContentAndAssert(quiz, playerId, buttonText, false)
    }

    @Test
    fun 실시간_퀴즈_시작_후에는_문제_생성이_불가하다() {
        val quiz = baseRealTimeQuiz.copy(isStarted = true)
        val buttonText = context.resources.getString(R.string.txt_close_create_question)
        setContentAndAssert(quiz, playerId, buttonText, false)
    }

    @Test
    fun 일반_퀴즈가_열리기_전_문제_생성이_가능하다() {
        val quiz = baseQuiz.copy(startTime = LocalDate.now().plusDays(1).toString())
        val buttonText = context.resources.getString(R.string.txt_open_create_question)
        setContentAndAssert(quiz, playerId, buttonText, true)
    }

    @Test
    fun 일반_퀴즈가_열린_후_문제_생성이_불가하다() {
        val quiz = baseQuiz.copy(startTime = LocalDate.now().minusDays(1).toString())
        val buttonText = context.resources.getString(R.string.txt_close_create_question)
        setContentAndAssert(quiz, playerId, buttonText, false)
    }

    @Test
    fun 일반_퀴즈가_열리기_전_퀴즈_참여가_불가하다() {
        val quiz = baseQuiz.copy(startTime = LocalDate.now().plusDays(1).toString())
        val buttonText = context.resources.getString(R.string.txt_quiz_start)
        setContentAndAssert(quiz, playerId, buttonText, false)
    }

    @Test
    fun 일반_퀴즈가_열린_후_퀴즈_참여가_가능하다() {
        val quiz = baseQuiz.copy(startTime = LocalDate.now().minusDays(1).toString())
        val buttonText = context.resources.getString(R.string.txt_quiz_start)
        setContentAndAssert(quiz, playerId, buttonText, true)
    }

    @Test
    fun 일반_퀴즈의_문제가_없는_경우_퀴즈_참여가_불가하다() {
        val quiz = baseQuiz.copy(questions = listOf())
        val buttonText = context.resources.getString(R.string.txt_quiz_question_count_zero)
        setContentAndAssert(quiz, playerId, buttonText, false)
    }
}
