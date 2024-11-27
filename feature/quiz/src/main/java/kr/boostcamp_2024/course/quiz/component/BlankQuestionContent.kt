package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionManager
import kr.boostcamp_2024.course.quiz.R

private const val NULL_BLANK_TEXT = "_"

@Composable
fun BlankQuestionContent(
    isOwner: Boolean = false,
    isRealTime: Boolean = false,
    questionTitle: String,
    contents: List<Map<String, Any>?>,
    blankWords: List<Map<String, Any>>,
    removeBlankWord: (Int) -> Unit,
    addBlankWord: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BlankQuestionTitle(title = questionTitle)
        BlankQuestionContents(
            isOwner = isOwner,
            contents = contents,
            onRemoveContentClick = removeBlankWord,
        )
        if (!isRealTime) {
            Blanks(
                words = blankWords,
                onAddContentClick = addBlankWord,
            )
        }
    }
}

@Composable
fun BlankQuestionTitle(
    title: String,
) {
    QuestionContentLabel(stringResource(R.string.txt_question_title))
    QuestionTextBox(
        text = title,
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlankQuestionContents(
    isOwner: Boolean,
    contents: List<Map<String, Any>?>,
    onRemoveContentClick: (Int) -> Unit,
) {
    QuestionContentLabel(stringResource(R.string.txt_question))

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        contents.forEachIndexed { index, content ->
            if (content?.get("type") == "text") {
                ConsumeTextContentUi(
                    word = content.getOrDefault("text", "") as String,
                    onContentRemove = {},
                    onValueChanged = { _, _ -> },
                    removeIconInvisible = false,
                    textFieldEnabled = false,
                    clickableEnabled = false,
                )
            } else {
                if (content == null) {
                    ConsumeBlankContentUi(
                        word = NULL_BLANK_TEXT,
                        onContentRemove = {},
                        onValueChanged = { _, _ -> },
                        removeIconVisible = false,
                        textFieldEnabled = false,
                        clickableEnabled = false,
                    )
                } else {
                    ConsumeBlankContentUi(
                        word = content.getOrDefault("text", "") as String,
                        onContentRemove = {
                            onRemoveContentClick(index)
                        },
                        onValueChanged = { _, _ -> },
                        removeIconVisible = false,
                        textFieldEnabled = false,
                        clickableEnabled = !isOwner,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Blanks(
    words: List<Map<String, Any>>,
    onAddContentClick: (Int) -> Unit,
) {
    QuestionContentLabel(stringResource(R.string.txt_blank))
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        words.forEachIndexed { index, word ->
            if (word["isUsed"] == false) {
                ConsumeBlankContentUi(
                    word = word["text"] as String,
                    onContentRemove = {
                        onAddContentClick(index)
                    },
                    onValueChanged = { _, _ -> },
                    textFieldEnabled = false,
                    removeIconVisible = false,
                )
            }
        }
    }
}

@Composable
fun QuestionContentLabel(
    label: String,
) {
    Text(
        label,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview
@Composable
private fun BlankQuestionContentPreview() {
    val question = BlankQuestion(
        id = "",
        title = "제목",
        solution = null,
        questionContent = listOf(
            mapOf("type" to "text", "text" to "우리는"),
            mapOf("type" to "blank", "text" to "위키즈팀"),
            mapOf("type" to "text", "text" to "입니다."),
        ),
        type = "blank",
        userAnswers = emptyList(),
    )
    val blankQuestionManager = BlankQuestionManager(
        updateCallback = {},
    )

    WeQuizTheme {
        BlankQuestionContent(
            isOwner = true,
            isRealTime = false,
            contents = blankQuestionManager.contents,
            questionTitle = question.title,
            blankWords = blankQuestionManager.blankWords,
            removeBlankWord = {},
            addBlankWord = {},
        )
    }
}
