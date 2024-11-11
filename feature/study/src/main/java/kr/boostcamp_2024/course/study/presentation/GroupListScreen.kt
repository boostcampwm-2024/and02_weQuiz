package kr.boostcamp_2024.course.study.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CustomPropertyTab
import kr.boostcamp_2024.course.study.component.GroupItem

@Composable
fun GroupListScreen() {
	var showDialog by remember { mutableStateOf(false) }
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(start = 16.dp, end = 16.dp, top = 8.dp),
	) {
		CustomPropertyTab(
			onClicked = { showDialog = true },
			imageVector = Icons.Outlined.AddCircle,
			title = R.string.property_tab_group_text,
		)
		if (showDialog) {
			CreateGroupScreen(
				onDismissButtonClick = { showDialog = false },
				onConfirmButtonClick = { showDialog = false },
			)
		}
		GroupLazyColumn()
	}
}

@Composable
fun GroupLazyColumn() {
	LazyColumn(
		modifier = Modifier
			.fillMaxWidth()
			.padding(),
	) {
		items(10) { index ->
			GroupItem(
				removeButtonClick = { Log.d("Group Screen", "제거됨") },
				profileImg = null,
				name = "홍길동",
			)

			if (index < 9) {
				HorizontalDivider()
			}
		}
	}
}
