package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeQuizImageLargeTopAppBar(
    topAppBarImageUrl: String?,
    scrollBehavior: TopAppBarScrollBehavior,
    title: @Composable (() -> Unit) = {},
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable (RowScope.() -> Unit) = {},
) {

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {

        var imageHeight by remember { mutableIntStateOf(0) }

        WeQuizAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { imageHeight.toDp() }),
            imgUrl = topAppBarImageUrl,
            contentDescription = null,
        )

        LargeTopAppBar(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(listOf(Color.Black, Color.Transparent)),
                    alpha = 0.6f,
                )
                .onSizeChanged { size ->
                    imageHeight = size.height
                },
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
            ),
            scrollBehavior = scrollBehavior,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun WeQuizImageLargeTopAppBarPreview() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    WeQuizTheme {
        WeQuizImageLargeTopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = "나는야 포켓몬 마스터 이다솜",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            topAppBarImageUrl = null,
            navigationIcon = {},
            actions = {},
        )
    }
}
