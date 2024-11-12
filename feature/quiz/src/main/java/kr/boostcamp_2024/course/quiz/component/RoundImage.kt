package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.quiz.R

@Composable
fun RoundImage(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape),
    ) {
        Image(
            painter = painterResource(id = R.drawable.quiz_system_profile),
            contentDescription = stringResource(R.string.des_image_question),
            modifier = Modifier.align(Alignment.Center),
            contentScale = ContentScale.Crop,
        )
    }
}
