package kr.boostcamp_2024.course.wequiz.ui

import androidx.compose.runtime.Composable
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
fun WeQuizApp() {
    WeQuizTheme {
        WeQuizNavHost()
    }
}
