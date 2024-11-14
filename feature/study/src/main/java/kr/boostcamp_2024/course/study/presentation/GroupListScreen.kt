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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CustomPropertyTab
import kr.boostcamp_2024.course.study.component.GroupItem

@Composable
fun GroupListScreen(
    currentGroup: StudyGroup,
    users: List<User>,
    removeClick: (String) -> Unit,
    inviteClick: (String, String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
    ) {
        CustomPropertyTab(
            studyGroupId = currentGroup.id,
            onClicked = { showDialog = true },
            imageVector = Icons.Outlined.AddCircle,
            title = R.string.property_tab_group_text,
            currentGroup = currentGroup,
        )
        if (showDialog) {
            CreateGroupDialog(
                onDismissButtonClick = { showDialog = false },
                onConfirmButtonClick = { groupId, email ->
                    inviteClick(groupId, email)
                    showDialog = false
                },
                groupId = currentGroup.id,
            )
        }
        GroupLazyColumn(users, removeClick)
    }
}

@Composable
fun GroupLazyColumn(users: List<User>, removeClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
    ) {
        itemsIndexed(items = users, key = { _, user -> user.id }) { index, user ->
            GroupItem(
                removeClick,
                user,
            )
            if (index < 9) {
                HorizontalDivider()
            }
        }
    }
}
