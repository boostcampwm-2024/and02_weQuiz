package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun TitleTextField(
    titleText: String,
    onTitleTextChange: (String) -> Unit,
    onClearTitleText: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = titleText,
            onValueChange = onTitleTextChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("제목") },
            placeholder = { Text("스터디 제목을 입력해주세요.") },
        )
        IconButton(
            onClick = onClearTitleText,
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                imageVector = Icons.Outlined.Cancel,
                contentDescription = "작성한 글 삭제"
            )
        }
    }
}