package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DescriptionTextField(
    descriptionText: String,
    onDescriptionTextChange: (String) -> Unit,
    onClearDescriptionText: () -> Unit,
    modifier: Modifier = Modifier
) {
    val placeholderText = "스터디 설명을 입력해주세요."

    Box(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = descriptionText,
            onValueChange = onDescriptionTextChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("설명") },
            placeholder = { Text(placeholderText) },
            minLines = 6,
            maxLines = 6,
        )
        IconButton(
            onClick = onClearDescriptionText,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Outlined.Cancel,
                contentDescription = null
            )
        }
    }
}