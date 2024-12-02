package kr.boostcamp_2024.course.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import kr.boostcamp_2024.course.main.R

@Composable
fun BaseGuideScreen(onGuideShown: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_arrow))
    val lottieAnimatable = rememberLottieAnimatable()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .clickable { onGuideShown() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.txt_main_guide),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 30.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopCenter)
                .padding(top = 25.dp)
                .offset(x = (screenWidth * 0.55f) - 200.dp)
                .graphicsLayer(scaleX = -1f),
        ) {
            LaunchedEffect(composition) {
                if (composition != null) {
                    lottieAnimatable.animate(
                        composition = composition,
                        iterations = Int.MAX_VALUE,
                    )
                }
            }
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
