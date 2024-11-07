package kr.boostcamp_2024.course.quiz.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTopBar(
    onShowDialog: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "퀴즈 제목") },
        navigationIcon = {
            IconButton(onClick = onShowDialog) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "뒤로가기"
                )
            }
        }
    )
}