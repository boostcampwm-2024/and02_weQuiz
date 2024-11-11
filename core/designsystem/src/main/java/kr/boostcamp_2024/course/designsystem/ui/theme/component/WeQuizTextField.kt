package kr.boostcamp_2024.course.designsystem.ui.theme.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.R

@Composable
fun WeQuizTextField(
	label: String,
	text: String,
	onTextChanged: (String) -> Unit,
	placeholder: String,
	minLines: Int = 1,
	maxLines: Int = 1,
) {
	TextField(
		value = text,
		onValueChange = { onTextChanged(it) },
		modifier = Modifier.fillMaxWidth(),
		textStyle = MaterialTheme.typography.bodyLarge,
		label = { Text(label) },
		placeholder = { Text(placeholder) },
		minLines = minLines,
		maxLines = maxLines,
		trailingIcon = {
			IconButton(
				onClick = { onTextChanged("") },
			) {
				Icon(
					painter = painterResource(R.drawable.outline_cancel_24),
					tint = MaterialTheme.colorScheme.onSurfaceVariant,
					contentDescription = stringResource(id = R.string.des_clear_text),
				)
			}
		},
	)
}

@Preview
@Composable
fun WeQuizTextFieldPreview() {
	WeQuizTextField(
		label = "Label",
		text = "Text",
		onTextChanged = {},
		placeholder = "Placeholder",
	)
}
