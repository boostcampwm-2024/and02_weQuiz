package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun CustomRoundImg(imgUrl: String?, modifier: Modifier = Modifier) {
    if (imgUrl != null) {
        AsyncImage(
            model =  ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }

    else {
        // 기본 이미지 또는 대체 UI 표시
        Box(
            modifier = modifier
                .background(Color.Gray)
        )
    }
}
