package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.viewmodel.BlankQuestionItem

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun CreateBlankQuestionContent(
    blankQuestionItems: List<BlankQuestionItem>,
    onContentRemove: (Int) -> Unit,
    onBlankQuestionItemValueChanged: (String, Int) -> Unit,
    onAddTextItemButtonClick: () -> Unit,
    isCreateTextButtonValid: Boolean,
    onAddBlankItemButtonClick: () -> Unit,
    isCreateBlankButtonValid: Boolean,
) {
    Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
            ),
            text = stringResource(R.string.txt_create_blank_question),
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            blankQuestionItems.forEachIndexed { index, it ->
                when (it) {
                    is BlankQuestionItem.Blank -> {
                        ConsumeBlankContentUi(
                            word = it.text,
                            index = index,
                            onContentRemove = onContentRemove,
                            onValueChanged = onBlankQuestionItemValueChanged,
                        )
                    }

                    is BlankQuestionItem.Text -> {
                        ConsumeTextContentUi(
                            word = it.text,
                            index = index,
                            onContentRemove = onContentRemove,
                            onValueChanged = onBlankQuestionItemValueChanged,
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            Button(
                onClick = onAddTextItemButtonClick,
                enabled = isCreateTextButtonValid,
            ) {
                Text(stringResource(R.string.btn_create_text))
            }

            Button(
                onClick = onAddBlankItemButtonClick,
                enabled = isCreateBlankButtonValid,
            ) {
                Text(stringResource(R.string.btn_create_blank))
            }
        }
    }
}
