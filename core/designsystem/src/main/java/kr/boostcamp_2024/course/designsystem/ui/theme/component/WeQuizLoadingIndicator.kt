package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
fun WeQuizCircularProgressIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .size(64.dp),
        )
    }
}

@Preview
@Composable
fun WeQuizCircularProgressIndicatorPreview() {
    WeQuizTheme {
        WeQuizCircularProgressIndicator()
    }
}
