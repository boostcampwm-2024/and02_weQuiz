package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
            CircleImg(
                categoryImg, modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16))
            )
            Box(modifier = Modifier.padding(top = 8.dp, end = 8.dp).align(Alignment.TopEnd)) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                ) {
                    Text(modifier = Modifier.align(Alignment.Center), text = quizCount.toString())
                }
            }
        }
    }
}
