package kr.boostcamp_2024.course.wequiz

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kr.boostcamp_2024.course.wequiz.ui.theme.WequizTheme

@Composable
fun WeQuizApp() {
    WequizTheme {
        Scaffold { innerPadding ->
            WeQuizNavHost(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}