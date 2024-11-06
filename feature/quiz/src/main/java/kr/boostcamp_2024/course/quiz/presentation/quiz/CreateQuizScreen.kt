package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    onNavigationButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "퀴즈 생성")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

    }
}


@Preview(showBackground = true)
@Composable
fun CreateQuizScreenPreview() {
    CreateQuizScreen(
        onNavigationButtonClick = {},
    )
}