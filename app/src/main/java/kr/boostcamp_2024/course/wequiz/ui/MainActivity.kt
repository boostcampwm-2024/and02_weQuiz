package kr.boostcamp_2024.course.wequiz.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                WeQuizApp()
        }
    }
}