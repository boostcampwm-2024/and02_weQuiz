package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.study.R

@Composable
fun CategoryItem(
    onClicked: () -> Unit,
    title: String,
    content: String,
    author: String,
    quizCount: Int,
    categoryImgUrl: String? = null,
    profileImgUrl: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuizCountBadge(categoryImgUrl, quizCount)
        DetailStudyDescription(title, content, profileImgUrl, author)
    }
}


@Composable
fun QuizCountBadge(categoryImg: String?, quizCount: Int) {
    Box(
        modifier = Modifier.size(120.dp),
    ) {
        WeQuizAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.extraLarge),
            imgUrl = categoryImg,
            contentDescription = null
        )
        Badge(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .align(Alignment.TopEnd),
            containerColor = MaterialTheme.colorScheme.error
        ) {
            Text(
                text = quizCount.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DetailStudyDescription(
    title: String,
    content: String,
    profileImg: String? = null,
    author: String
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(modifier = Modifier.padding(bottom = 4.dp), text = title, style = MaterialTheme.typography.titleLarge)
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
        AuthorComponent(profileImg, author)
    }
}

@Composable
fun AuthorComponent(
    profileImg: String? = null,
    author: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        WeQuizAsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp),
            imgUrl = profileImg,
            contentDescription = stringResource(R.string.des_study_detail_category_item_profile),
        )
        Text(modifier = Modifier.padding(start = 4.dp), text = author, style = MaterialTheme.typography.bodySmall)
    }
}
