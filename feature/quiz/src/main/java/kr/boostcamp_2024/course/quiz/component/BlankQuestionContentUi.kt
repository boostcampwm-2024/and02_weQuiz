package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.R

@Composable
fun ConsumeBlankContentUi(
    word: String,
    onContentRemove: () -> Unit,
    removeIconInvisible: Boolean = true,
    onValueChanged: (String) -> Unit,
    textFieldEnabled: Boolean = true,
) {
    ElevatedCard(
        modifier = Modifier.shadow(50.dp),
        shape = ShapeDefaults.Small,
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .clickable(
                    enabled = true,
                    onClick = onContentRemove,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            BasicTextField(
                value = word,
                onValueChange = onValueChanged,
                modifier = Modifier.width(IntrinsicSize.Min),
                enabled = textFieldEnabled,
            )
            if (removeIconInvisible) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = stringResource(R.string.des_remove_blank),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun ConsumeTextContentUi(
    word: String,
    onContentRemove: () -> Unit,
    removeIconInvisible: Boolean = true,
    onValueChanged: (String) -> Unit,
    textFieldEnabled: Boolean = true,
) {
    Column(
        modifier = Modifier.drawBehind {
            val borderSize = 1.dp.toPx()
            drawLine(
                color = Color.Black,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = borderSize,
            )
        },
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .clickable(
                    enabled = true,
                    onClick = onContentRemove,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BasicTextField(
                value = word,
                onValueChange = onValueChanged,
                modifier = Modifier.width(IntrinsicSize.Min),
                enabled = textFieldEnabled,
            )
            if (removeIconInvisible) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = stringResource(R.string.des_remove_blank),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

        }

    }

}

@Preview
@Composable
private fun ConsumeBlankContentUiPreview() {
    WeQuizTheme {
        ConsumeBlankContentUi(
            word = "판다",
            onContentRemove = {},
            onValueChanged = {},
            textFieldEnabled = true,
        )

    }
}

@Preview
@Composable
private fun ConsumeTextContentUiPreview() {
    WeQuizTheme {
        ConsumeTextContentUi(
            word = "는",
            onContentRemove = {},
            onValueChanged = {},
            textFieldEnabled = true,
        )
    }
}
