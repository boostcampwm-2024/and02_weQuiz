package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CustomPropertyTab
import kr.boostcamp_2024.course.study.component.GroupItem

@Composable
fun GroupListScreen(
    currentGroup: StudyGroup?,
    owner: User?,
    curUserId: String?,
    users: List<User>,
    inviteClick: (String, String) -> Unit,
    removeClick: (String, String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val isOwner: Boolean = owner?.id == curUserId
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
    ) {
        CustomPropertyTab(
            studyGroupId = currentGroup?.id ?: "",
            onClicked = { _, _ -> showDialog = true },
            imageVector = Icons.Outlined.AddCircle,
            title = R.string.property_tab_group_text,
            currentGroup = currentGroup ?: StudyGroup("", "", "", "", 0, "", emptyList(), emptyList()),
        )
        if (showDialog) {
            CreateGroupDialog(
                onDismissButtonClick = { showDialog = false },
                onConfirmButtonClick = { groupId, email ->
                    inviteClick(groupId, email)
                    showDialog = false
                },
                groupId = currentGroup?.id ?: "",
            )
        }
        GroupLazyColumn(
            owner,
            currentGroup?.id,
            isOwner,
            users,
            removeClick,
        )
    }
}

@Composable
fun GroupLazyColumn(
    owner: User?,
    groupId: String?,
    isOwner: Boolean,
    users: List<User>,
    removeClick: (String, String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
    ) {
        itemsIndexed(items = users, key = { _, user -> user.id }) { index, user ->
            GroupItem(
                owner?.id,
                groupId,
                isOwner,
                removeClick,
                user,
            )
            if (index < 9) {
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true, locale = "ko")
@Composable
fun GroupListScreenPreview() {
    MaterialTheme {
        GroupListScreen(
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
            owner = User(
                id = "user1",
                name = "user1",
                email = "email1",
                profileUrl = "",
                studyGroups = emptyList(),
            ),
            curUserId = "user1",
            users = listOf(
                User(
                    id = "user1",
                    name = "user1",
                    email = "email1",
                    profileUrl = "",
                    studyGroups = emptyList(),
                ),
                User(
                    id = "user2",
                    name = "user2",
                    email = "email2",
                    profileUrl = "",
                    studyGroups = emptyList(),
                ),
                User(
                    id = "user3",
                    name = "user3",
                    email = "email3",
                    profileUrl = "",
                    studyGroups = emptyList(),
                ),
            ),
            inviteClick = { _, _ -> },
            removeClick = { _, _ -> },
        )
    }
}
