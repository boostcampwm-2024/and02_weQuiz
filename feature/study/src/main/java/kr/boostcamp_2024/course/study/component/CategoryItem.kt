package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .fillMaxWidth().padding(vertical = 8.dp)
            .clickable(onClick = onClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuizCountBadge(categoryImgUrl, quizCount)
        DetailStudyDescription(title, content, profileImgUrl, author)
    }
}


@Composable
fun QuizCountBadge(categoryImg: String?, quizCount: Int) {
    BadgedBox(
        modifier = Modifier.size(120.dp),
        badge = {
            Badge(
                modifier = Modifier.size(24.dp),
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Text(
                    text = quizCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) {
        CustomRoundImg(
            categoryImg, modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large)
        )
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
            .fillMaxHeight().padding(16.dp)
    ) {
        Text(modifier = Modifier.padding(bottom = 4.dp), text = title, style = MaterialTheme.typography.titleLarge)
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
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
    Row(modifier = Modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        CustomRoundImg(
            profileImg, modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
        )
        Text(modifier = Modifier.padding(start = 4.dp), text = author, style = MaterialTheme.typography.bodySmall)
    }
}
