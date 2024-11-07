package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun CircleImage(
    imagePainter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .clip(CircleShape),
        painter = imagePainter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}