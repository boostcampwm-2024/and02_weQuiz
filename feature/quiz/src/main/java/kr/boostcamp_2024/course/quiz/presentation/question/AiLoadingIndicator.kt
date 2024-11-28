package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import kr.boostcamp_2024.course.quiz.R

@Composable
fun AiLoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_ai))
        val lottieAnimatable = rememberLottieAnimatable()

        LaunchedEffect(composition) {
            if (composition != null) {
                lottieAnimatable.animate(
                    composition = composition,
                    iterations = Int.MAX_VALUE,
                )
            }
        }

        Text(
            text = "문제를 생성 중입니다...",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        LottieAnimation(
            composition = composition,
            progress = { lottieAnimatable.progress },
            modifier = Modifier.size(200.dp),
        )
        Text(
            text = "AI도 실수를 할 수 있습니다!",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
    }
}
