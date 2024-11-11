package kr.boostcamp_2024.course.main.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.main.component.Notification
import kr.boostcamp_2024.course.main.component.NotificationItem
import kr.boostcamp_2024.course.main.component.NotificationTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(onNavigationButtonClick: () -> Unit) {
	val notifications = remember { generateDummyNotifications() } // Todo 뷰모델로 바꿔주기

	Scaffold(topBar = {
		NotificationTopAppBar(onNavigationButtonClick = onNavigationButtonClick)
	}) { paddingValues ->
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
		) {
			items(notifications.size) { index ->
				NotificationItem(
					notifications[index],
					onRejectClick = {},
					onAcceptClick = {},
				) //API 통신 시 바꿔주기
			}
		}
	}
}

// 임시 활용 data 생성 함수
fun generateDummyNotifications(): List<Notification> {
	return List(10) {
		Notification(groupId = 1111)
	}
}

@Preview
@Composable
private fun NotificationScreenPreview() {
	NotificationScreen {}
}
