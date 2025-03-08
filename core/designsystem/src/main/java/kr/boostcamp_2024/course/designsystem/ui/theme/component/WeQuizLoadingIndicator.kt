package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.R
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
fun WeQuizCircularProgressIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val indicatorContentDes = stringResource(R.string.des_loading_indicator)
        CircularProgressIndicator(
            modifier = modifier
                .semantics { contentDescription = indicatorContentDes }
                .size(64.dp),
        )
    }
}

@PreviewKoLightDark
@Composable
private fun WeQuizCircularProgressIndicatorPreview() {
    WeQuizTheme {
        WeQuizCircularProgressIndicator()
    }
}
