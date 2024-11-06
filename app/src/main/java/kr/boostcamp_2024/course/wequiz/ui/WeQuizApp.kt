package kr.boostcamp_2024.course.wequiz.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.presentation.quiz.CreateQuizScreen

@Composable
fun WeQuizApp() {
    WeQuizTheme {
        Scaffold { innerPadding ->
            CreateQuizScreen(
                onNavigationButtonClick = {},
            )
        }
    }
}