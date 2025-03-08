package kr.boostcamp_2024.course.quiz

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kr.boostcamp_2024.course.domain.model.ChoiceQuestionCreationInfo
import kr.boostcamp_2024.course.quiz.presentation.question.CreateQuestionScreen
import kr.boostcamp_2024.course.quiz.viewmodel.BlankQuestionItem
import kr.boostcamp_2024.course.quiz.viewmodel.CreateQuestionUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateQuestionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var baseCreateQuestionUiState: CreateQuestionUiState
    private lateinit var baseChoiceQuestionCreationInfo: ChoiceQuestionCreationInfo
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        baseChoiceQuestionCreationInfo = ChoiceQuestionCreationInfo(
            title = "title",
            description = "description",
            solution = "solution",
            choices = listOf("choice1", "choice2", "choice3", "choice4"),
            answer = 0,
        )
        baseCreateQuestionUiState = CreateQuestionUiState(choiceQuestionCreationInfo = baseChoiceQuestionCreationInfo)
    }

    private fun setCreateQuestionScreen(uiState: CreateQuestionUiState) {
        composeTestRule.setContent {
            CreateQuestionScreen(
                uiState = uiState,
                onTitleChanged = {},
                onDescriptionChanged = {},
                onSolutionChanged = {},
                onNavigationButtonClick = {},
                onChoiceTextChanged = { _, _ -> },
                onSelectedChoiceNumChanged = {},
                onCreateQuestionButtonClick = {},
                onQuestionTypeIndexChange = {},
                onAddBlankItemButtonClick = {},
                onAddTextItemButtonClick = {},
                onBlankQuestionItemValueChanged = { _, _ -> },
                onContentRemove = {},
                onCreateBlankQuestionButtonClick = {},
                onShowDialog = {},
            )
        }
    }

    private fun assertCreateQuestionButtonEnableValue(isEnabled: Boolean) {
        val buttonText = context.resources.getString(R.string.btn_create_question)
        val buttonNode = composeTestRule.onAllNodesWithText(buttonText)
            .filter(
                hasClickAction(),
            ).onFirst()

        if (isEnabled) {
            buttonNode.assertIsEnabled()
        } else {
            buttonNode.assertIsNotEnabled()
        }
    }

    private fun scrollUntilCreateQuestionButtonVisible() {
        val createQuestionButtonContentDes = context.resources.getString(R.string.btn_create_question)
        composeTestRule.onNode(hasScrollAction())
            .assertExists()
            .performScrollToNode(
                hasText(createQuestionButtonContentDes),
            )
    }

    @Test
    fun 로딩_상태의_경우_로딩_인디케이터를_표시한다() {
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            isLoading = true,
        )
        setCreateQuestionScreen(newCreateQuestionUiState)

        composeTestRule.onNodeWithContentDescription(
            context.resources.getString(kr.boostcamp_2024.course.designsystem.R.string.des_loading_indicator),
        ).assertExists()
    }

    // 일반 문제 test
    @Test
    fun 일반_문제_생성_시_4개의_객관식_문항을_작성할_수_있다() {
        val generalQuestionIndex = 0
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = generalQuestionIndex,
        )
        setCreateQuestionScreen(newCreateQuestionUiState)
        // 문제 생성 버튼이 보일 때까지 스크롤
        scrollUntilCreateQuestionButtonVisible()
        // 객관식 문항이 4개 생성되었는지 확인
        composeTestRule.onAllNodesWithContentDescription(
            context.resources.getString(R.string.des_choice_item),
        ).assertCountEquals(4)
    }

    @Test
    fun 일반_문제_생성_시_객관식_문항에_공백이_있으면_문제_생성이_불가하다() {
        val generalQuestionIndex = 0
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = generalQuestionIndex,
            choiceQuestionCreationInfo = baseChoiceQuestionCreationInfo.copy(
                choices = listOf("choice1", "choice2", "choice3", ""),
            ),
        )
        setCreateQuestionScreen(newCreateQuestionUiState)
        // 문제 생성 버튼이 보일 때까지 스크롤
        scrollUntilCreateQuestionButtonVisible()
        // 문제 생성 버튼이 비활성화 되어있는지 확인
        assertCreateQuestionButtonEnableValue(false)
    }

    @Test
    fun 일반_문제_생성_시_정답_객관식_번호는_0에서_3_사이여야_한다() {
        val generalQuestionIndex = 0
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = generalQuestionIndex,
            choiceQuestionCreationInfo = baseChoiceQuestionCreationInfo.copy(
                answer = 4,
            ),
        )

        setCreateQuestionScreen(newCreateQuestionUiState)
        // 문제 생성 버튼이 보일 때까지 스크롤
        scrollUntilCreateQuestionButtonVisible()
        // 문제 생성 버튼이 비활성화 되어있는지 확인
        assertCreateQuestionButtonEnableValue(false)
    }

    @Test
    fun 일반_문제_생성_시_제목을_필수로_작성해야_한다() {
        val generalQuestionIndex = 0
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = generalQuestionIndex,
            choiceQuestionCreationInfo = baseChoiceQuestionCreationInfo.copy(
                title = "",
            ),
        )

        setCreateQuestionScreen(newCreateQuestionUiState)
        // 문제 생성 버튼이 보일 때까지 스크롤
        scrollUntilCreateQuestionButtonVisible()
        // 문제 생성 버튼이 비활성화 되어있는지 확인
        assertCreateQuestionButtonEnableValue(false)
    }

    @Test
    fun 일반_문제_생성_시_설명을_필수로_작성해야_한다() {
        val generalQuestionIndex = 0
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = generalQuestionIndex,
            choiceQuestionCreationInfo = baseChoiceQuestionCreationInfo.copy(
                description = "",
            ),
        )

        setCreateQuestionScreen(newCreateQuestionUiState)
        // 문제 생성 버튼이 보일 때까지 스크롤
        scrollUntilCreateQuestionButtonVisible()
        // 문제 생성 버튼이 비활성화 되어있는지 확인
        assertCreateQuestionButtonEnableValue(false)
    }

    // 낱말 맞추기 문제 test
    @Test
    fun 낱말_맞추기_문제_생성_시_낱말_요소와_텍스트_요소를_생성할_수_있다() {
        val blankQuestionIndex = 1
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = blankQuestionIndex,
        )
        setCreateQuestionScreen(newCreateQuestionUiState)
        composeTestRule.onNode(
            hasText(context.resources.getString(R.string.btn_create_text)),
        ).assertExists()

        composeTestRule.onNode(
            hasText(context.resources.getString(R.string.btn_create_blank)),
        ).assertExists()
    }

    @Test
    fun 낱말_맞추기_문제_생성_시_적어도_하나의_빈칸_요소를_포함해야_한다() {
        val blankQuestionIndex = 1
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = blankQuestionIndex,
            items = listOf(BlankQuestionItem.Text("text1")),
        )
        setCreateQuestionScreen(newCreateQuestionUiState)
        // 문제 생성 버튼이 보일 떄까지 스크롤
        scrollUntilCreateQuestionButtonVisible()
        // 문제 생성 버튼이 비활성화 되어있는지 확인
        assertCreateQuestionButtonEnableValue(false)
    }

    @Test
    fun 낱말_맞추기_문제_생성_시_빈칸_요소는_최대_5개까지만_추가할_수_있다() {
        val blankQuestionIndex = 1
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = blankQuestionIndex,
            choiceQuestionCreationInfo = baseChoiceQuestionCreationInfo,
            items = listOf(
                BlankQuestionItem.Blank("blank1"),
                BlankQuestionItem.Blank("blank2"),
                BlankQuestionItem.Blank("blank3"),
                BlankQuestionItem.Blank("blank4"),
                BlankQuestionItem.Blank("blank5"),
            ),
        )

        setCreateQuestionScreen(newCreateQuestionUiState)
        // blank 요소 생성 버튼이 보일 때까지 스크롤
        val buttonText = context.resources.getString(R.string.btn_create_blank)
        composeTestRule.onNode(hasScrollAction())
            .performScrollToNode(hasText(buttonText))
        // blank 요소 생성 버튼이 비활성화 되어있는지 확인
        composeTestRule.onNodeWithText(buttonText).assertIsNotEnabled()
    }

    @Test
    fun 낱말_맞추기_문제_생성_시_텍스트_요소는_최대_5개까지만_추가할_수_있다() {
        val blankQuestionIndex = 1
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = blankQuestionIndex,
            items = listOf(
                BlankQuestionItem.Blank("blank1"),
                BlankQuestionItem.Text("text1"),
                BlankQuestionItem.Text("text2"),
                BlankQuestionItem.Text("text3"),
                BlankQuestionItem.Text("text4"),
                BlankQuestionItem.Text("text5"),
            ),
        )

        setCreateQuestionScreen(newCreateQuestionUiState)

        val buttonText = context.resources.getString(R.string.btn_create_text)
        // text 요소 생성 버튼이 보일 때까지 스크롤
        composeTestRule.onNode(hasScrollAction())
            .performScrollToNode(hasText(buttonText))
        // text 요소 생성 버튼이 비활성화 되어있는지 확인
        composeTestRule.onNodeWithText(buttonText).assertIsNotEnabled()
    }

    @Test
    fun 낱말_맞추기_문제_생성_시_빈칸_및_텍스트_아이템들은_텍스트_길이가_1_이상이어야_한다() {
        val blankQuestionIndex = 1
        val newCreateQuestionUiState = CreateQuestionUiState(
            selectedQuestionTypeIndex = blankQuestionIndex,
            items = listOf(
                BlankQuestionItem.Text("text1"),
                BlankQuestionItem.Blank("blank1"),
                BlankQuestionItem.Text("text2"),
                BlankQuestionItem.Blank(""),
                BlankQuestionItem.Text("text3"),
                BlankQuestionItem.Blank("blank3"),
            ),
        )

        setCreateQuestionScreen(newCreateQuestionUiState)
        // 문제 생성 버튼이 보일 떄까지 스크롤
        scrollUntilCreateQuestionButtonVisible()
        // 문제 생성 버튼이 비활성화 되어있는지 확인
        assertCreateQuestionButtonEnableValue(false)
    }

    @Test
    fun 낱말_맞추기_문제_생성_시_제목은_필수로_작성해야_한다() {
        val blankQuestionIndex = 1
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = blankQuestionIndex,
            choiceQuestionCreationInfo = baseChoiceQuestionCreationInfo.copy(
                title = "",
            ),
        )

        setCreateQuestionScreen(newCreateQuestionUiState)
        // 문제 생성 버튼이 보일 떄까지 스크롤
        scrollUntilCreateQuestionButtonVisible()
        // 문제 생성 버튼이 비활성화 되어있는지 확인
        assertCreateQuestionButtonEnableValue(false)
    }

    @Test
    fun 낱말_맞추기_문제_생성_시_유저는_추가한_빈칸_및_텍스트를_확인하고_클릭할_수_있다() {
        val blankQuestionIndex = 1
        val textItemsTextList = listOf("text1", "text2")
        val blankItemsTextList = listOf("blank1", "blank2")
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = blankQuestionIndex,
            items = listOf(
                BlankQuestionItem.Text(textItemsTextList[0]),
                BlankQuestionItem.Blank(blankItemsTextList[0]),
                BlankQuestionItem.Text(textItemsTextList[1]),
                BlankQuestionItem.Blank(blankItemsTextList[1]),
            ),
        )
        setCreateQuestionScreen(newCreateQuestionUiState)
        // text 요소 생성 버튼이 보일 때까지 스크롤
        val buttonText = context.resources.getString(R.string.btn_create_text)
        composeTestRule.onNode(hasScrollAction())
            .performScrollToNode(hasText(buttonText))

        // text 및 blank 아이템들 확인
        textItemsTextList.forEach { textItemText ->
            val textItemContentDes = context.resources.getString(R.string.des_text_item, textItemText)
            composeTestRule.onNodeWithContentDescription(textItemContentDes)
                .assertExists()
                .assertIsEnabled()
        }

        blankItemsTextList.forEach { blankItemText ->
            val blankItemContentDes = context.resources.getString(R.string.des_blank_item, blankItemText)
            composeTestRule.onNodeWithContentDescription(blankItemContentDes)
                .assertExists()
                .assertIsEnabled()
        }
    }

    // ai test
    @Test
    fun 일반_퀴즈_생성_시_ai_문제_추천을_받을_수_있다() {
        val generalQuestionIndex = 0
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = generalQuestionIndex,
        )
        setCreateQuestionScreen(newCreateQuestionUiState)

        val aiSuggestionFabContentDes = context.resources.getString(R.string.btn_create_quiz_ai)
        composeTestRule.onNodeWithContentDescription(aiSuggestionFabContentDes)
            .assertIsEnabled()
    }

    @Test
    fun 낱말_맞추기_문제_생성_시_ai_문제_추천을_받을_수_없다() {
        val blankQuestionIndex = 1
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            selectedQuestionTypeIndex = blankQuestionIndex,
        )
        setCreateQuestionScreen(newCreateQuestionUiState)

        val aiSuggestionFabContentDes = context.resources.getString(R.string.btn_create_quiz_ai)
        composeTestRule.onNodeWithContentDescription(aiSuggestionFabContentDes)
            .assertDoesNotExist()
    }

    @Test
    fun ai_로딩_상태의_경우_ai_로딩_인디케이터를_표시한다() {
        val newCreateQuestionUiState = baseCreateQuestionUiState.copy(
            isAiLoading = true,
        )
        setCreateQuestionScreen(newCreateQuestionUiState)

        val aiLoadingIndicatorContentDes = context.resources.getString(R.string.des_ai_loading)
        composeTestRule.onNodeWithContentDescription(aiLoadingIndicatorContentDes)
            .assertExists()
    }

}
