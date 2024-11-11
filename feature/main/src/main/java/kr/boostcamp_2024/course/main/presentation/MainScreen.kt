package kr.boostcamp_2024.course.main.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.main.R
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {},
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
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateStudyButtonClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.des_fab_create_study)
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(
                    PaddingValues(  // Ignore Top Padding
                        start = innerPadding.calculateLeftPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateRightPadding(LocalLayoutDirection.current),
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )
        ) {
            UserContent(user = uiState.user)
            UserStudyContent(
                studyGroups = uiState.studyGroups,
                onStudyGroupClick = onStudyGroupClick
            )
        }
    }
}

@Composable
fun UserContent(
    user: User? = null
) {
    val configuration = LocalConfiguration.current

    val imageHeight = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 150.dp
        else -> 300.dp
    }

    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight),
            painter = painterResource(id = R.drawable.sample),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart),
            text = user?.name ?: "",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStudyContent(
    studyGroups: List<StudyGroup>,
    onStudyGroupClick: () -> Unit,
) {

    var state by remember { mutableIntStateOf(0) }
    val titles = listOf(
        stringResource(R.string.tab_participating_study),
        stringResource(R.string.tab_storage)
    )

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
            StudyTab(
                studyGroups = studyGroups,
                onStudyGroupClick = onStudyGroupClick
            )
        }

        1 -> {
            // TODO: 보관함
        }
    }
}

@Composable
fun StudyTab(
    studyGroups: List<StudyGroup>,
    onStudyGroupClick: () -> Unit,
) {
    LazyColumn {
        items(items = studyGroups, key = { it.name }) {     // TODO key
            StudyGroupItem(
                studyGroup = it,
                onStudyGroupClick = onStudyGroupClick
            )
        }
    }
}

@Composable
fun StudyGroupItem(
    studyGroup: StudyGroup,
    onStudyGroupClick: () -> Unit,
) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon( // TODO studyUrl
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.outlineVariant),
                imageVector = Icons.Outlined.Star,
                contentDescription = stringResource(R.string.des_img_study_image)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onStudyGroupClick)
            ) {
                Text(
                    text = studyGroup.name,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (studyGroup.description.isNotBlank()) {
                    Text(
                        text = studyGroup.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = stringResource(R.string.text_study_user_count, studyGroup.users.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { /* TODO : ex) 스터디 나가기 */ }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.des_btn_study_menu)
                )
            }
        }

        HorizontalDivider(thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onNotificationButtonClick = {},
        onCreateStudyButtonClick = {},
        onStudyGroupClick = {},
    )
}