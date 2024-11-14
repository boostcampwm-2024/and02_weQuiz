package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RadioTextButton(
    text: String,
    selected: Boolean,
    onclick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onclick),
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            enabled = false,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(14.dp),
        )
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(top = 10.dp, bottom = 10.dp, end = 7.dp),
        )
    }
}
