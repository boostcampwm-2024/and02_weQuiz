package kr.boostcamp_2024.course.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizImageLargeTopAppBar
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.main.R
import kr.boostcamp_2024.course.main.component.StudyGroupItem
import kr.boostcamp_2024.course.main.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNotificationButtonClick: () -> Unit,
    onCreateStudyButtonClick: () -> Unit,
    onStudyGroupClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        currentUser = uiState.currentUser,
        studyGroups = uiState.studyGroups,
        onNotificationButtonClick = onNotificationButtonClick,
        onCreateStudyButtonClick = onCreateStudyButtonClick,
        onStudyGroupClick = onStudyGroupClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentUser: User? = null,
    studyGroups: List<StudyGroup> = emptyList(),
    onNotificationButtonClick: () -> Unit,
    onCreateStudyButtonClick: () -> Unit,
    onStudyGroupClick: () -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var state by rememberSaveable { mutableIntStateOf(0) }
    val titles = listOf(
        stringResource(R.string.tab_participating_study),
        stringResource(R.string.tab_storage)
    )

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
                        modifier = Modifier.padding(16.dp),
                        text = currentUser?.name ?: "",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = { },
                actions = {
                    IconButton(onClick = onNotificationButtonClick) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = stringResource(
                                R.string.des_btn_notification
                            )
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                onClick = onCreateStudyButtonClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.des_fab_create_study)
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }

            when (state) {
                0 -> {
                    StudyGroupTab(
                        studyGroups = studyGroups,
                        onStudyGroupClick = onStudyGroupClick
                    )
                }

                1 -> { /* TODO 보관함 */
                }
            }
        }
    }
}

@Composable
fun StudyGroupTab(
    studyGroups: List<StudyGroup>,
    onStudyGroupClick: () -> Unit,
) {
    LazyColumn {
        items(items = studyGroups, key = { it.id }) { studyGroup ->
            StudyGroupItem(
                studyGroup = studyGroup,
                onStudyGroupClick = onStudyGroupClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WeQuizTheme {
        MainScreen(
            currentUser = User(
                email = "email@email.com",
                name = "홍준표",
                profileUrl = "testUrl",
                studyGroups = listOf()
            ),
            studyGroups = listOf(
                StudyGroup(
                    id = "1234",
                    name = "일본어 스터디",
                    studyGroupImageUrl = null,
                    description = "일본어 스터디그룹 와압~!",
                    maxUserNum = 12,
                    ownerId = "test",
                    users = listOf("test"),
                    categories = emptyList()
                )
            ),
            onNotificationButtonClick = {},
            onCreateStudyButtonClick = {},
            onStudyGroupClick = {}
        )
    }
}