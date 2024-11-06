package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomPropertyTab(
    onClicked: () -> Unit,
    imageVector: ImageVector,
    description: String? = null,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        CustomIconButton(onClicked = onClicked, imageVector = imageVector, description = description)
    }
}
