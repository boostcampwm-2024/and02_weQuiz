package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CategoryItem
import kr.boostcamp_2024.course.study.component.CustomPropertyTab

@Composable
// TODO : categories 적용하기
fun CategoryListScreen(categories: List<Category>, createCategoryClick: () -> Unit, categoryItemClick: () -> Unit) {
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
        CategoryLazyColumn(categories, categoryItemClick)
    }
}

@Composable
fun CategoryLazyColumn(categories: List<Category>, categoryItemClick: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(items = categories, key = { _, category -> category.id }) { index, category ->
            CategoryItem(categoryItemClick, categories.size, category, "", "")
            if (index < categories.size) {
                HorizontalDivider()
            }
        }
    }
}
