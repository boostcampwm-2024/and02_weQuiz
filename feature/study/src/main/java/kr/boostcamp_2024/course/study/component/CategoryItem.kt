package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.study.R

@Composable
internal fun CategoryItem(
    category: Category,
    profileUrl: String?,
    author: String,
    currentGroupId: String,
    onCategoryItemClick: (String, String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = { onCategoryItemClick(currentGroupId, category.id) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuizCountBadge(
            categoryImg = category.categoryImageUrl,
            quizCount = category.quizzes.size,
        )
        DetailStudyDescription(
            title = category.name,
            content = category.description ?: stringResource(R.string.txt_detail_study_no_category_description),
            profileUrl = profileUrl,
            author = author,
        )
    }
}

@Composable
private fun QuizCountBadge(
    categoryImg: String?,
    quizCount: Int,
) {
    Box(
        modifier = Modifier.size(120.dp),
    ) {
        WeQuizAsyncImage(
            modifier = Modifier.clip(MaterialTheme.shapes.extraLarge),
            imgUrl = categoryImg,
            contentDescription = null,
        )
        Badge(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .align(Alignment.TopEnd),
            containerColor = MaterialTheme.colorScheme.error,
        ) {
            Text(
                text = quizCount.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun DetailStudyDescription(
    title: String,
    content: String,
    profileUrl: String? = null,
    author: String,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        AuthorComponent(
            author = author,
            profileUrl = profileUrl,
        )
    }
}

@Composable
private fun AuthorComponent(
    author: String,
    profileUrl: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WeQuizAsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp),
            imgUrl = profileUrl,
            contentDescription = stringResource(R.string.des_study_detail_category_item_profile),
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = author,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun CategoryItemPreview() {
    WeQuizTheme {
        CategoryItem(
            category = Category(
                id = "CategoryId1",
                name = "Category1",
                description = "Description1",
                categoryImageUrl = "",
                quizzes = emptyList(),
            ),
            profileUrl = "",
            author = "Author1",
            currentGroupId = "GroupId1",
            onCategoryItemClick = { _, _ -> },
        )
    }
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun QuizCountBadgePreview() {
    WeQuizTheme {
        QuizCountBadge(
            categoryImg = "",
            quizCount = 5,
        )
    }
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun DetailStudyDescriptionPreview() {
    WeQuizTheme {
        DetailStudyDescription(
            title = "Title",
            content = "Content",
            profileUrl = "",
            author = "Author",
        )
    }
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun AuthorComponentPreview() {
    WeQuizTheme {
        AuthorComponent(
            profileUrl = "",
            author = "Author",
        )
    }
}
