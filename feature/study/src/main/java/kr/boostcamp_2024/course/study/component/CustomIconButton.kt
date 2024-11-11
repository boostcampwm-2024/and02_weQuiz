package kr.boostcamp_2024.course.study.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomIconButton(
	onClicked: () -> Unit,
	imageVector: ImageVector,
	description: String? = null,
) {
	IconButton(onClick = onClicked) {
		Icon(imageVector = imageVector, contentDescription = description)
	}
}
