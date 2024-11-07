package kr.boostcamp_2024.course.study.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun CustomIconButton(
    onClicked: () -> Unit,
    icon: Painter,
    description: String? = null
) {
    IconButton(onClick = onClicked) {
        Icon(painter = icon, contentDescription = description)
    }
}
