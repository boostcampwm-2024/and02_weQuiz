package kr.boostcamp_2024.course.login.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(
	modifier: Modifier = Modifier,
	text: String,
	shape: RoundedCornerShape,
	align: Alignment.Horizontal = Alignment.Start,
) {
	Column(
		modifier = modifier.fillMaxWidth(),
	) {
		Box(
			modifier = modifier
				.align(align)
				.background(
					color = MaterialTheme.colorScheme.secondaryContainer,
					shape = shape,
				)
				.padding(horizontal = 16.dp, vertical = 8.dp),
		) {
			Text(
				text = text,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Bold,
			)
		}
	}
}
