package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.study.R

@Composable
fun StudyCreationButton(onCreateStudySuccess: () -> Unit, modifier: Modifier = Modifier) {
	Button(
		onClick = onCreateStudySuccess,
		modifier = modifier
			.fillMaxWidth()
			.padding(16.dp),
	) {
		Text(
			text = stringResource(R.string.txt_study_creation_button),
			modifier = modifier,
			style = MaterialTheme.typography.bodyMedium.copy(
				fontWeight = FontWeight.Medium,
			),
		)
	}
}
