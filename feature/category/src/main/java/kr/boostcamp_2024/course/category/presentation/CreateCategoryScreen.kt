package kr.boostcamp_2024.course.category.presentation

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
fun CreateCategoryScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategorySuccess: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onNavigationButtonClick,
        ) {
            Text(text = "뒤로가기 버튼")
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "카테고리 생성 화면")
            Button(onClick = onCreateCategorySuccess) {
                Text(text = "카테고리 생성")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCategoryScreenPreview() {
    CreateCategoryScreen(
        onNavigationButtonClick = {},
        onCreateCategorySuccess = {},
    )
}
