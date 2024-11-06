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
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.main.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNotificationButtonClick: () -> Unit,
    onCreateStudyButtonClick: () -> Unit,
    onStudyClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = onNotificationButtonClick) {
                        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateStudyButtonClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
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
            UserContent()
            UserStudyContent(onStudyClick = onStudyClick)
        }
    }
}

@Composable
fun UserContent() {
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
            text = "홍길동",
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
    onStudyClick: () -> Unit,
) {

    var state by remember { mutableStateOf(0) }
    val titles = listOf("참여 스터디", "보관함")

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
            StudyTab(onStudyClick = onStudyClick)
        }

        1 -> {
            // TODO: 보관함
        }
    }
}

@Composable
fun StudyTab(
    onStudyClick: () -> Unit,
) {
    LazyColumn {
        item {
            StudyItem(
                studyUrl = "",
                studyTitle = "안드로이드 개발자",
                studyDescription = "안드로이드 개발자를 위한 스터디입니다.",
                studyMember = 3,
                onStudyClick = onStudyClick
            )
        }

        item {
            StudyItem(
                studyUrl = "",
                studyTitle = "OS 스터디",
                studyDescription = "os 와압!",
                studyMember = 10,
                onStudyClick = onStudyClick
            )
        }

        item {
            StudyItem(
                studyUrl = "",
                studyTitle = "웹개발자",
                studyDescription = "웹개발자를 위한 스터디입니다.",
                studyMember = 5,
                onStudyClick = onStudyClick
            )
        }

        item {
            StudyItem(
                studyUrl = "",
                studyTitle = "네트워크 스터디",
                studyDescription = "",
                studyMember = 5,
                onStudyClick = onStudyClick
            )
        }

        item {
            StudyItem(
                studyUrl = "",
                studyTitle = "일본어 스터디",
                studyDescription = "곤니찌와~",
                studyMember = 6,
                onStudyClick = onStudyClick
            )
        }

        item {
            StudyItem(
                studyUrl = "",
                studyTitle = "요리왕 비룡",
                studyDescription = "비 내리는 날엔 난 항상 널 그리워 해.",
                studyMember = 6,
                onStudyClick = onStudyClick
            )
        }
    }
}

@Composable
fun StudyItem(
    studyUrl: String,
    studyTitle: String,
    studyDescription: String,
    studyMember: Int,
    onStudyClick: () -> Unit,
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
                imageVector = Icons.Outlined.Star, contentDescription = null
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onStudyClick)
            ) {
                Text(
                    text = studyTitle,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (studyDescription.isNotEmpty()) {
                    Text(
                        text = studyDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "인원: ${studyMember}명",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { /* TODO : ex) 스터디 나가기 */ }
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }

        HorizontalDivider(thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun StudyItemPreview() {
    StudyItem(
        studyUrl = "",
        studyTitle = "안드로이드 개발자",
        studyDescription = "안드로이드 개발자를 위한 스터디입니다.",
        studyMember = 3,
        onStudyClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onNotificationButtonClick = {},
        onCreateStudyButtonClick = {},
        onStudyClick = {},
    )
}