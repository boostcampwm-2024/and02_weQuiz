package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kr.boostcamp_2024.course.designsystem.R

@Composable
fun WeQuizLocalRoundedImage(
    modifier: Modifier = Modifier,
    imagePainter: Painter,
    contentDescription: String?,
) {
    Image(
        modifier = modifier.clip(CircleShape),
        painter = imagePainter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun WeQuizAsyncImage(
    modifier: Modifier = Modifier,
    imgUrl: String,
    placeholder: Painter = painterResource(id = R.drawable.img_error),
    error: Painter = painterResource(id = R.drawable.img_error),
    contentDescription: String?
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imgUrl)
            .crossfade(true)
            .build(),
        modifier = modifier,
        placeholder = placeholder,
        error = error,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
    )
}