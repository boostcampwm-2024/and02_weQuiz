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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.category.R
import kr.boostcamp_2024.course.category.viewModel.CategoryViewModel
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.Quiz

@Composable
fun CategoryScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateQuizButtonClick: () -> Unit,
    onQuizClick: () -> Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
) {
    val categoryUiState = categoryViewModel.categoryUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        categoryUiState.value.snackBarMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            categoryViewModel.setNewSnackBarMessage(null)
        }
    }

    CategoryScreen(
        category = categoryUiState.value.category,
        quizList = categoryUiState.value.quizList,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateQuizButtonClick = onCreateQuizButtonClick,
        onQuizClick = onQuizClick,
        setNewSnackBarMessage = categoryViewModel::setNewSnackBarMessage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryScreen(
    category: Category?,
    quizList: List<Quiz>?,
    onNavigationButtonClick: () -> Unit,
    onCreateQuizButtonClick: () -> Unit,
    onQuizClick: () -> Unit,
    setNewSnackBarMessage: (String) -> Unit,
) {
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
                        onClick = {
                            // todo: 카테고리 설정
                            setNewSnackBarMessage("추후 제공될 기능입니다.")
                        },
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
                onClick = onCreateQuizButtonClick,
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
        if (category != null) {
            Column(
                modifier = Modifier.padding(innerPadding),
            ) {
                CategoryContent(
                    categoryTitle = category.name,
                    categoryDescription = category.description,
                )
                QuizList(
                    modifier = Modifier.weight(1f),
                    quizzes = quizList,
                    onQuizClick = onQuizClick,
                )
            }
        } else {
            WeQuizCircularProgressIndicator()
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

@Composable
fun QuizList(
    modifier: Modifier = Modifier,
    quizzes: List<Quiz>?,
    onQuizClick: () -> Unit,
) {
    if (quizzes != null) {
        LazyVerticalGrid(
            modifier = modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(3),
        ) {
            items(
                items = quizzes,
                key = { it.id },
            ) { quiz ->
                QuizItem(
                    quiz = quiz,
                    onQuizClick = onQuizClick,
                )
            }
        }
    } else {
        WeQuizCircularProgressIndicator()
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
