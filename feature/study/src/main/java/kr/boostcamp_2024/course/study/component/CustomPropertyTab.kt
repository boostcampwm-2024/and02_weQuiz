package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.study.R

@Composable
internal fun CustomPropertyTab(
    imageVector: ImageVector,
    title: String,
    currentGroup: StudyGroup,
    onTabClick: (String?, String?) -> Unit,
    description: String? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(onClick = { onTabClick(currentGroup.id, null) }) {
            Icon(
                imageVector = imageVector,
                contentDescription = description,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AssistChip(
            onClick = { /* no-op */ },
            label = {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.assist_chip_detail_study_group_member_number, currentGroup.users.size, currentGroup.maxUserNum),
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(R.string.assist_chip_top_bar_detail_study),
                )
            },
        )
    }
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun CustomPropertyTabPreview() {
    WeQuizTheme {
        CustomPropertyTab(
            imageVector = Icons.Outlined.AddCircle,
            title = "카테고리",
            currentGroup = StudyGroup(
                id = "study1",
                name = "study1",
                description = "description",
                maxUserNum = 12,
                studyGroupImageUrl = "",
                ownerId = "",
                users = emptyList(),
                categories = emptyList(),
            ),
            onTabClick = { _, _ -> },
        )
    }
}
