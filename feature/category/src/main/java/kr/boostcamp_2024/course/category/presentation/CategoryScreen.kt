package kr.boostcamp_2024.course.category.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CategoryScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuizButtonClick: () -> Unit,
    onQuizClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onNavigationButtonClick
        ) {
            Text(text = "뒤로가기 버튼")
        }

        Button(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = onCreateQuizButtonClick
        ) {
            Text(text = "퀴즈 생성")
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "진행 중")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "진행 완료")
                }
            }

            Text(text = "카테고리 퀴즈 화면")
            Button(onClick = onQuizClick) {
                Text(text = "퀴즈")
            }
            Button(onClick = onQuizClick) {
                Text(text = "퀴즈")
            }
            Button(onClick = onQuizClick) {
                Text(text = "퀴즈")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    CategoryScreen(
        onNavigationButtonClick = {},
        onCreateQuizButtonClick = {},
        onQuizClick = {},
    )
}