package kr.boostcamp_2024.course.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizImageLargeTopAppBar
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.main.R
import kr.boostcamp_2024.course.main.component.BaseGuideScreen
import kr.boostcamp_2024.course.main.component.GuideDialog
import kr.boostcamp_2024.course.main.component.MainDropDownMenu
import kr.boostcamp_2024.course.main.component.StudyGroupItem
import kr.boostcamp_2024.course.main.viewmodel.MainViewModel

@Composable
fun MainScreen(
    onNotificationButtonClick: () -> Unit,
    onCreateStudyButtonClick: () -> Unit,
    onStudyGroupClick: (String) -> Unit,
    onEditStudyButtonClick: (String) -> Unit,
    onEditUserClick: (String?) -> Unit,
    onLogOutClick: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        currentUser = uiState.currentUser,
        studyGroups = uiState.studyGroups,
        notifications = uiState.notificationNumber,
        snackBarHostState = snackBarHostState,
        onNotificationButtonClick = onNotificationButtonClick,
        onCreateStudyButtonClick = onCreateStudyButtonClick,
        onEditStudyGroupClick = onEditStudyButtonClick,
        onDeleteStudyGroupClick = viewModel::deleteStudyGroup,
        onLeaveStudyGroupClick = viewModel::deleteUserFromStudyGroup,
        onStudyGroupClick = onStudyGroupClick,
        onEditUserClick = onEditUserClick,
        onLogOutClick = viewModel::logout,
    )

    if (!uiState.isGuideShown) {
        BaseGuideScreen { viewModel.onGuideShown() }
    }

    if (uiState.isLogout) {
        onLogOutClick()
    }

    if (uiState.isLoading) {
        WeQuizCircularProgressIndicator()
    }

    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackBarHostState.showSnackbar(errorMessage)
            viewModel.shownErrorMessage()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentUser: User?,
    studyGroups: List<StudyGroup>,
    notifications: Int,
    snackBarHostState: SnackbarHostState,
    onNotificationButtonClick: () -> Unit,
    onCreateStudyButtonClick: () -> Unit,
    onEditStudyGroupClick: (String) -> Unit,
    onDeleteStudyGroupClick: (StudyGroup) -> Unit,
    onLeaveStudyGroupClick: (String) -> Unit,
    onStudyGroupClick: (String) -> Unit,
    onEditUserClick: (String?) -> Unit,
    onLogOutClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var userMenuIsExpanded by remember { mutableStateOf(false) }
    var showGuideDialog by rememberSaveable { mutableStateOf(false) }
    var state by rememberSaveable { mutableIntStateOf(0) }
    val titles = stringArrayResource(R.array.main_tabs_titles)

    if (showGuideDialog) {
        GuideDialog(
            guideUrl = stringResource(R.string.guide_url),
            onDismissButtonClick = { showGuideDialog = false },
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            WeQuizImageLargeTopAppBar(
                topAppBarImageUrl = currentUser?.profileUrl,
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = currentUser?.name ?: "",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { userMenuIsExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = stringResource(R.string.top_app_bar_nav_btn),
                        )
                    }
                    MainDropDownMenu(
                        isExpanded = userMenuIsExpanded,
                        onDismissRequest = { userMenuIsExpanded = false },
                        onEditUserClick = {
                            if (currentUser?.id != null) {
                                onEditUserClick(currentUser.id)
                            }
                        },
                        onLogOutClick = onLogOutClick,
                    )
                },
                actions = {
                    IconButton(onClick = { showGuideDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.LibraryBooks,
                            stringResource(R.string.des_main_guide_icon),
                        )
                    }
                    IconButton(onClick = onNotificationButtonClick) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = stringResource(R.string.des_btn_notification),
                            )
                            Badge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 5.dp, y = (-4).dp)
                                    .size(16.dp),
                                containerColor = MaterialTheme.colorScheme.error,
                            ) {
                                Text(
                                    text = notifications.toString(),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                onClick = onCreateStudyButtonClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.des_fab_create_study),
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            PrimaryTabRow(selectedTabIndex = state) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier.padding(top = 8.dp),
                        selected = state == index,
                        onClick = { state = index },
                        text = {
                            Text(
                                text = title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                    )
                }
            }

            when (state) {
                0 -> {
                    StudyGroupTab(
                        currentUser = currentUser,
                        studyGroups = studyGroups,
                        onStudyGroupClick = onStudyGroupClick,
                        onEditStudyGroupClick = onEditStudyGroupClick,
                        onDeleteStudyGroupClick = onDeleteStudyGroupClick,
                        onLeaveStudyGroupClick = onLeaveStudyGroupClick,
                    )
                }

                1 -> {
                    ArchiveTab()
                }
            }
        }
    }
}

@Composable
fun ArchiveTab() {
    // TODO: 보관함 기능 구현
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.txt_no_implementation),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun StudyGroupTab(
    currentUser: User?,
    studyGroups: List<StudyGroup>,
    onStudyGroupClick: (String) -> Unit,
    onEditStudyGroupClick: (String) -> Unit,
    onDeleteStudyGroupClick: (StudyGroup) -> Unit,
    onLeaveStudyGroupClick: (String) -> Unit,
) {
    LazyColumn {
        items(items = studyGroups, key = { it.id }) { studyGroup ->
            StudyGroupItem(
                isOwner = (studyGroup.ownerId == currentUser?.id),
                studyGroup = studyGroup,
                onStudyGroupClick = onStudyGroupClick,
                onEditStudyGroupClick = onEditStudyGroupClick,
                onDeleteStudyGroupClick = onDeleteStudyGroupClick,
                onLeaveStudyGroupClick = onLeaveStudyGroupClick,
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, locale = "ko")
@Composable
fun MainScreenPreview() {
    WeQuizTheme {
        MainScreen(
            currentUser = User(
                id = "user1",
                email = "email@email.com",
                name = "아이비",
                profileUrl = "",
                studyGroups = listOf(),
            ),
            studyGroups = listOf(
                StudyGroup(
                    id = "study1",
                    name = "안드로이드 스터디",
                    studyGroupImageUrl = null,
                    description = "안드로이드 스터디입니다.",
                    maxUserNum = 12,
                    ownerId = "user1",
                    users = listOf("user1", "user2"),
                    categories = emptyList(),
                ),
            ),
            snackBarHostState = SnackbarHostState(),
            onEditStudyGroupClick = {},
            onLeaveStudyGroupClick = {},
            onNotificationButtonClick = {},
            onCreateStudyButtonClick = {},
            onStudyGroupClick = {},
            onEditUserClick = {},
            onLogOutClick = {},
            onDeleteStudyGroupClick = {},
            notifications = 0,
        )
    }
}
