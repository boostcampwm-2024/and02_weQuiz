package kr.boostcamp_2024.course.study.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizImageLargeTopAppBar
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.component.CustomIconButton
import kr.boostcamp_2024.course.study.navigation.DetailScreenRoute
import kr.boostcamp_2024.course.study.navigation.GroupScreenRoute
import kr.boostcamp_2024.course.study.viewmodel.DetailStudyViewModel

@Composable
fun DetailStudyScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onDetailStudyGroupSuccess: () -> Unit,
    viewModel: DetailStudyViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailStudyScreen(
        currentGroup = uiState.currentGroup,
        categories = uiState.categories,
        users = uiState.users,
        owner = uiState.owner,
        currentUserId = uiState.userId,
        snackBarHostState = snackBarHostState,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateCategoryButtonClick = onCreateCategoryButtonClick,
        onCategoryClick = onCategoryClick,
        onRemoveStudyGroupMemberButtonClick = viewModel::deleteStudyGroupMember,
        onInviteConfirmButtonClick = viewModel::addNotification,
        onEditStudyGroupClick = {},
        onDeleteStudyGroupClick = viewModel::deleteStudyGroup,
        onLeaveStudyGroupClick = viewModel::deleteUserFromStudyGroup,
    )

    if (uiState.isLoading) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
            )
        }
    }

    uiState.errorMessageId?.let { errorMessageId ->
        val errorMessage = stringResource(errorMessageId)
        LaunchedEffect(errorMessageId) {
            snackBarHostState.showSnackbar(errorMessage)
            viewModel.shownErrorMessage()
        }
    }

    if (uiState.isDeleteStudyGroupSuccess) {
        LaunchedEffect(Unit) {
            onDetailStudyGroupSuccess()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailStudyScreen(
    currentGroup: StudyGroup?,
    categories: List<Category>,
    users: List<User>,
    owner: User?,
    currentUserId: String?,
    snackBarHostState: SnackbarHostState,
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onRemoveStudyGroupMemberButtonClick: (String, String) -> Unit,
    onInviteConfirmButtonClick: (String, String) -> Unit,
    onEditStudyGroupClick: () -> Unit,
    onDeleteStudyGroupClick: () -> Unit,
    onLeaveStudyGroupClick: () -> Unit,
) {
    var selectedScreenIndex by remember { mutableIntStateOf(0) }
    val screenList = listOf(DetailScreenRoute, GroupScreenRoute)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            WeQuizImageLargeTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = currentGroup?.name ?: "",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                topAppBarImageUrl = currentGroup?.studyGroupImageUrl,
                navigationIcon = {
                    CustomIconButton(
                        onClicked = onNavigationButtonClick,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        description = stringResource(R.string.btn_detail_study_top_bar_back),
                    )
                },
                actions = {
                    currentGroup?.let {
                        StudyDropDownMenu(
                            isOwner = (owner?.id == currentUserId),
                            onEditStudyGroupClick = onEditStudyGroupClick,
                            onDeleteStudyGroupClick = onDeleteStudyGroupClick,
                            onLeaveStudyGroupClick = onLeaveStudyGroupClick,
                        )
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                screenList.forEachIndexed { index, screen ->
                    val selected = selectedScreenIndex == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            selectedScreenIndex = index
                        },
                        label = { Text(text = stringResource(screen.title)) },
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.iconId),
                                contentDescription = stringResource(R.string.des_icon_bottom_nav_detail_study),
                            )
                        },
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { innerPadding ->
        if (currentGroup != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                when (selectedScreenIndex) {
                    0 -> CategoryListScreen(
                        owner,
                        currentGroup,
                        categories,
                        onCreateCategoryButtonClick,
                        onCategoryClick,
                    )

                    1 -> GroupListScreen(
                        currentGroup,
                        owner,
                        currentUserId,
                        users,
                        onInviteConfirmButtonClick,
                        onRemoveStudyGroupMemberButtonClick,
                    )
                }
            }
        }
    }
}

@Composable
fun StudyDropDownMenu(
    isOwner: Boolean,
    onEditStudyGroupClick: () -> Unit,
    onDeleteStudyGroupClick: () -> Unit,
    onLeaveStudyGroupClick: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        CustomIconButton(
            onClicked = { isExpanded = isExpanded.not() },
            imageVector = Icons.Filled.Settings,
            description = stringResource(R.string.btn_top_bar_detail_study_setting),
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            when (isOwner) {
                true -> {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.txt_study_group_menu_edit)) },
                        onClick = {
                            onEditStudyGroupClick()
                            isExpanded = false
                        },
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.txt_study_group_menu_delete)) },
                        onClick = {
                            onDeleteStudyGroupClick()
                            isExpanded = false
                        },
                    )
                }

                false -> {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.txt_study_group_menu_leave)) },
                        onClick = {
                            onLeaveStudyGroupClick()
                            isExpanded = false
                        },
                    )
                }
            }
        }
    }
}
