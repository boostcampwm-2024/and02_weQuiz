package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kr.boostcamp_2024.course.study.R

@Composable
fun DescriptionTextField(
	descriptionText: String,
	onDescriptionTextChange: (String) -> Unit,
	onClearDescriptionText: () -> Unit,
	modifier: Modifier = Modifier,
) {

	Box(modifier = modifier.fillMaxWidth()) {
		TextField(
			value = descriptionText,
			onValueChange = onDescriptionTextChange,
			modifier = Modifier.fillMaxWidth(),
			label = { Text(stringResource(R.string.txt_create_study_description_label)) },
			placeholder = { Text(stringResource(R.string.txt_create_study_description_placeholder)) },
			minLines = 6,
			maxLines = 6,
		)
		IconButton(
			onClick = onClearDescriptionText,
			modifier = Modifier.align(Alignment.TopEnd),
		) {
			Icon(
				imageVector = Icons.Outlined.Cancel,
				contentDescription = stringResource(R.string.des_clear_text),
			)
		}
	}
}
