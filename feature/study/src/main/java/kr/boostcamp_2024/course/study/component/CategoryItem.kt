package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryItem(
    onClicked: () -> Unit,
    categoryImg: String? = null,
    title: String,
    content: String,
    profileImg: String? = null,
    author: String,
    quizCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .clickable(onClick = onClicked)
    ) {
        Box(modifier = Modifier.size(120.dp)) {
            CustomRoundImg(
                categoryImg, modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16))
            )
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .align(Alignment.TopEnd)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = quizCount.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        DetailStudyDescription(title, content, profileImg, author)
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
            .padding(start = 16.dp)
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
