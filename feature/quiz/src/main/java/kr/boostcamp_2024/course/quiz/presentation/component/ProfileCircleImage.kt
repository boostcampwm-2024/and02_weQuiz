package kr.boostcamp_2024.course.quiz.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kr.boostcamp_2024.course.quiz.R

@Composable
fun ProfileCircleImage(
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .clip(CircleShape),
        painter = painterResource(id = R.drawable.sample_profile),
        contentDescription = "Profile",
        contentScale = ContentScale.Crop
    )
}