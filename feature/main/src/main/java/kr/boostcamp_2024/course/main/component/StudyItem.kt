package kr.boostcamp_2024.course.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.main.R

@Composable
fun StudyItem(
	studyUrl: String,
	studyTitle: String,
	studyDescription: String,
	studyMember: Int,
	onStudyClick: () -> Unit,
) {

	Column {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 12.dp),
			horizontalArrangement = Arrangement.spacedBy(16.dp),
		) {
			Icon(
				modifier = Modifier
					.size(56.dp)
					.clip(MaterialTheme.shapes.large)
					.background(MaterialTheme.colorScheme.outlineVariant),
				imageVector = Icons.Outlined.Star,
				contentDescription = stringResource(R.string.des_img_study_image),
			)

			Column(
				modifier = Modifier
					.weight(1f)
					.clickable(onClick = onStudyClick),
			) {
				Text(
					text = studyTitle,
					style = MaterialTheme.typography.bodyLarge,
				)

				if (studyDescription.isNotEmpty()) {
					Text(
						text = studyDescription,
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
					)
				}

				Text(
					text = stringResource(R.string.text_study_user_count, studyMember),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
				)
			}

			IconButton(
				modifier = Modifier.size(24.dp),
				onClick = { /* TODO : ex) 스터디 나가기 */ },
			) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = stringResource(R.string.des_btn_study_menu),
				)
			}
		}

		HorizontalDivider(thickness = 1.dp)
	}
}

@Preview(showBackground = true)
@Composable
fun StudyItemPreview() {
	StudyItem(
		studyUrl = "",
		studyTitle = "안드로이드 개발자",
		studyDescription = "안드로이드 개발자를 위한 스터디입니다.",
		studyMember = 3,
		onStudyClick = {},
	)
}
