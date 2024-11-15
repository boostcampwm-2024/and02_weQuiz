package kr.boostcamp_2024.course.study.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.study.R

@Composable
fun GroupItem(
    ownerId: String?,
    groupId: String?,
    isOwner: Boolean,
    removeButtonClick: (String, String) -> Unit,
    user: User,
) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        WeQuizAsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(54.dp),
            imgUrl = user.profileUrl,
            contentDescription = stringResource(R.string.des_study_detail_group_profile),
        )
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            text = user.name,
            style = MaterialTheme.typography.bodyLarge,
        )
        if (isOwner && ownerId != user.id) {
            Button(
                onClick = {
                    if (groupId != null) {
                        removeButtonClick(user.id, groupId)
                    }
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_remove_24),
                    contentDescription = stringResource(R.string.des_detail_study_remove_group_btn),
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.btn_remove_group),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
