package kr.boostcamp_2024.course.wequiz.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@Composable
fun WeQuizApp() {
    WeQuizTheme {
        WeQuizNavHost(modifier = Modifier.fillMaxSize())
    }
}