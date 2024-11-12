package kr.boostcamp_2024.course.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.main.R

@Composable
fun StudyGroupItem(
    studyGroup: StudyGroup,
    onStudyGroupClick: () -> Unit,
    onStudyGroupMenuClick: () -> Unit,
) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeQuizAsyncImage(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.large),
                imgUrl = studyGroup.studyGroupImageUrl,
                contentDescription = stringResource(R.string.des_img_study_image),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onStudyGroupClick)
            ) {
                Text(
                    text = studyGroup.name,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (studyGroup.description.isNullOrBlank().not()) {
                    Text(
                        text = studyGroup.description!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = stringResource(R.string.txt_study_user_count, studyGroup.users.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = onStudyGroupMenuClick
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.des_btn_study_menu)
                )
            }
        }

        HorizontalDivider(thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun StudyGroupItemPreview() {
    WeQuizTheme {
        StudyGroupItem(
            studyGroup = StudyGroup(
                id = "1234",
                name = "일본어 스터디",
                studyGroupImageUrl = null,
                description = "일본어 스터디 그룹 와압~!",
                maxUserNum = 12,
                ownerId = "test",
                users = listOf("test"),
                categories = emptyList()
            ),
            onStudyGroupClick = {},
            onStudyGroupMenuClick = {},
        )
    }
}