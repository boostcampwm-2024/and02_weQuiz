package kr.boostcamp_2024.course.study.presentation

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
fun StudyScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    onCategoryClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onNavigationButtonClick,
        ) {
            Text(text = "뒤로가기")
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = onCreateCategoryButtonClick) {
                Text(text = "카테고리 생성")
            }
            Text(text = "스터디 화면")
            Button(onClick = onCategoryClick) {
                Text(text = "카테고리")
            }
            Button(onClick = onCategoryClick) {
                Text(text = "카테고리")
            }
            Button(onClick = onCategoryClick) {
                Text(text = "카테고리")
            }
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "카테고리")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "그룹원")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudyScreenPreview() {
    StudyScreen(
        onNavigationButtonClick = {},
        onCreateCategoryButtonClick = {},
        onCategoryClick = {},
    )
}
