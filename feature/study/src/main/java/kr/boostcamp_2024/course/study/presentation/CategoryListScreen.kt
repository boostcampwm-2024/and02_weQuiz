package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CategoryItem
import kr.boostcamp_2024.course.study.component.CustomPropertyTab

@Composable
fun CategoryListScreen(createCategoryClick: () -> Unit, categoryItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
    ) {
        CustomPropertyTab(
            onClicked = createCategoryClick,
            imageVector = Icons.Outlined.AddCircle,
            title = R.string.property_tab_category_text,
        )
        CategoryLazyColumn(categoryItemClick)
    }
}

@Composable
fun CategoryLazyColumn(categoryItemClick: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(10) { index ->
            CategoryItem(
                onClicked = categoryItemClick,
                title = "스레드",
                content = "카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요. 카테고리 소개를 적어주세요.",
                author = "내가 퀴즈 낸 사람이다.",
                quizCount = 3,
                categoryImgUrl = null,
                profileImgUrl = null,
            )

            if (index < 9) {
                HorizontalDivider()
            }
        }
    }
}
