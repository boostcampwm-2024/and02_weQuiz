package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizOutLinedTextField
import kr.boostcamp_2024.course.quiz.R

@Composable
fun CreateChoiceItems(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    choices: List<String>,
    selectedChoiceNum: Int,
    updateChoiceText: (Int, String) -> Unit,
    updateSelectedChoiceNum: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        var keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        var keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })

        choices.forEachIndexed { index, choiceText ->
            if (index == choices.size - 1) {
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            }
            ChoiceItem(
                text = choiceText,
                onTextChanged = { updateChoiceText(index, it) },
                isSelected = selectedChoiceNum == index,
                onSelected = { updateSelectedChoiceNum(index) },
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
            )
        }
    }
}

@Composable
fun ChoiceItem(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onSelected(!isSelected)
            },
        )
        WeQuizOutLinedTextField(
            modifier = Modifier.weight(1f),
            text = text,
            onTextChanged = onTextChanged,
            placeholder = stringResource(id = R.string.txt_question_choice_placeholder),
            minLines = 1,
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
    }
}
