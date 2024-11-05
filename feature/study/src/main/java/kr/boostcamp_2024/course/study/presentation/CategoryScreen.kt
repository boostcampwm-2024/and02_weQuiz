package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CustomPropertyTab

@Composable
fun CategoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        CustomPropertyTab(onClicked = {}, icon = painterResource(R.drawable.baseline_add_circle_outline_24), title = "카테고리")
    }
}
