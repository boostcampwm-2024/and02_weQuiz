package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

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
        modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.onSizeChanged { size ->
                imageHeight = size.height
            },
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            ),
            scrollBehavior = scrollBehavior
        )
    }
}
