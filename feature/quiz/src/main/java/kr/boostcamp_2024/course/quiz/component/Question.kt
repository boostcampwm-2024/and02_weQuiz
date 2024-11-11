package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Question(
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp).height(900.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        //TODO(객관식 문항 통신으로 받아오기)
        val options = listOf(
            "1번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.1번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.1번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.1번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.",
            "2번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.2번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.2번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.2번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.",
            "3번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.3번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.3번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.3번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.",
            "4번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.4번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.4번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.4번 객관식 문항 내용입니다. 이것도 전체 다 보여줌.",
        )

        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .selectable(
                        selected = selectedIndex == index,
                        onClick = { onOptionSelected(index) },
                    ),
            ) {
                RadioButton(
                    selected = selectedIndex == index,
                    onClick = null,
                    modifier = Modifier.padding(8.dp),
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically),
                )
            }
        }
    }
}
