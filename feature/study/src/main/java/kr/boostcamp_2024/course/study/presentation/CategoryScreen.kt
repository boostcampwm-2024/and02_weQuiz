package kr.boostcamp_2024.course.study.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CategoryItem
import kr.boostcamp_2024.course.study.component.CustomPropertyTab

@Composable
fun CategoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        CustomPropertyTab(onClicked = {}, icon = painterResource(R.drawable.baseline_add_circle_outline_24), title = "카테고리")
        CategoryLazyColumn()
    }
}

@Composable
fun CategoryLazyColumn() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(10) { index ->
            CategoryItem(
                onClicked = { Log.d("detail", "lazy column 아이템 클릭됨") },
                categoryImg = null,
                title = "스레드",
                content = "카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요.",
                profileImg = null,
                author = "내가 퀴즈 낸 사람이다.",
                quizCount = 3
            )

            if (index < 9) {
                HorizontalDivider()
            }
        }
    }
}
