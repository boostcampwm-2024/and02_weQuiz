package kr.boostcamp_2024.course.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.domain.model.Notification
import kr.boostcamp_2024.course.domain.model.NotificationWithGroupInfo
import kr.boostcamp_2024.course.main.R
import kr.boostcamp_2024.course.main.component.NotificationItem
import kr.boostcamp_2024.course.main.component.NotificationTopAppBar
import kr.boostcamp_2024.course.main.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel<NotificationViewModel>(),
    onNavigationButtonClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val notificationInfos = uiState.notificationWithGroupInfoList
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState) {
        uiState.snackBarMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            viewModel.onSnackBarShown()
        }
    }

    NotificationScreen(
        notificationInfos = notificationInfos,
        onRejectClick = viewModel::deleteInvitation,
        onAcceptClick = viewModel::acceptInvitation,
        onNavigationButtonClick = onNavigationButtonClick,
        snackBarHostState = snackBarHostState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    notificationInfos: List<NotificationWithGroupInfo>,
    onRejectClick: (String) -> Unit,
    onAcceptClick: (Notification) -> Unit,
    onNavigationButtonClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            NotificationTopAppBar(onNavigationButtonClick = onNavigationButtonClick)
        },
    ) { paddingValues ->
        if (notificationInfos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(R.string.txt_blank_notification))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                items(
                    items = notificationInfos,
                    key = { it.notification.id },
                ) { notificationInfo ->
                    notificationInfo.studyGroupName ?: return@items
                    NotificationItem(
                        notificationInfo = notificationInfo,
                        onRejectClick = { onRejectClick(notificationInfo.notification.id) },
                        onAcceptClick = { onAcceptClick(notificationInfo.notification) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotificationScreenPreview() {
    WeQuizTheme {
        NotificationScreen(
            notificationInfos = listOf(
                NotificationWithGroupInfo(
                    notification = Notification(
                        id = "1",
                        groupId = "1",
                        userid = "1",
                    ),
                    studyGroupName = "스터디 이름",
                    studyGroupImgUrl = "null",
                ),
            ),
            onRejectClick = {},
            onAcceptClick = {},
            onNavigationButtonClick = {},
            snackBarHostState = SnackbarHostState(),
        )
    }
}
