package kr.boostcamp_2024.course.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen(
    onNotificationButtonClick: () -> Unit,
    onCreateStudyButtonClick: () -> Unit,
    onStudyClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onNotificationButtonClick
        ) {
            Text(text = "알림")
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "메인 화면")
            Button(onClick = onStudyClick) {
                Text(text = "스터디")
            }
            Button(onClick = onStudyClick) {
                Text(text = "스터디")
            }
            Button(onClick = onStudyClick) {
                Text(text = "스터디")
            }
        }

        Button(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = onCreateStudyButtonClick
        ) {
            Text(text = "스터디 생성")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onNotificationButtonClick = {},
        onCreateStudyButtonClick = {},
        onStudyClick = {},
    )
}