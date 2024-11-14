package kr.boostcamp_2024.course.category.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.category.R
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.Quiz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuizButtonClick: () -> Unit,
    onQuizClick: () -> Unit,
) {
    val dummyCategory = Category(
        id = "tbaGgtjOlxx7m6ATBGmu",
        name = "안드 마스터",
        description = "안드로이드 마스터가 되어 보아요!!",
        categoryImageUrl = null,
        quizzes = listOf("1", "1", "1", "1", "1", "1", "1", "1", "1", "1"),
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* no - op */ },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigationButtonClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.des_btn_back),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* todo: 카테고리 설정 */ },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.des_btn_settings),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateQuizButtonClick() },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.des_fab_create_quiz),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            CategoryContent(
                categoryTitle = dummyCategory.name,
                categoryDescription = dummyCategory.description,
            )
            QuizTabs(
                selectedTabIndex = selectedTabIndex,
                onTabClick = { index -> selectedTabIndex = index },
            )
            if (selectedTabIndex == 0) {
                QuizList(
                    modifier = Modifier.weight(1f),
                    quizzes = dummyCategory.quizzes,
                    onQuizClick = onQuizClick,
                )
            }
        }
    }
}

@Composable
fun CategoryContent(
    categoryTitle: String,
    categoryDescription: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = categoryTitle,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        categoryDescription?.let { description ->
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTabs(
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit,
) {
    val tabs = stringArrayResource(R.array.quiz_tabs)
    PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier.padding(top = 8.dp),
                selected = selectedTabIndex == index,
                onClick = { onTabClick(index) },
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
}

@Composable
fun QuizList(
    modifier: Modifier = Modifier,
    quizzes: List<String>,
    onQuizClick: () -> Unit,
) {
    val tmpQuiz = Quiz(
        id = "1",
        title = "안드로이드 퀴즈",
        description = "안드로이드 퀴즈를 풀어보세요!",
        startTime = "2021-09-01",
        solveTime = 10,
        questions = emptyList(),
        userOmrs = emptyList(),
    )

    LazyVerticalGrid(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(3),
    ) {
        items(quizzes) { quiz ->
            QuizItem(
                quiz = tmpQuiz,
                onQuizClick = onQuizClick,
            )

        }
    }
}

@Composable
fun QuizItem(
    quiz: Quiz,
    onQuizClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable { onQuizClick() },
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        WeQuizAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
            imgUrl = null,
            contentDescription = null,
        )

        Text(
            text = quiz.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // todo: date format
        Text(
            text = quiz.startTime,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    WeQuizTheme {
        CategoryScreen(
            onNavigationButtonClick = {},
            onCreateQuizButtonClick = {},
            onQuizClick = {},
        )
    }
}
