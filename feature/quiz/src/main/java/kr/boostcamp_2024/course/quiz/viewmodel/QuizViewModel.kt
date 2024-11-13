package kr.boostcamp_2024.course.quiz.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.boostcamp_2024.course.domain.model.Quiz
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor() : ViewModel() {
    private val _quizState = MutableStateFlow(
        Quiz(
            id = "2k1QrCuOUHLERgQAmMqg",
            title = "생활용어",
            description = "뿌셔뿌셔 불고기",
            startTime = "2024-12-03",
            solveTime = 70,
            questions = listOf(
                "OJSAMgoMx4mwuwyg9JLo",
                "AP60qaPeHDfwJ7OGZygb",
                "VNsXOZvL9K85Nl0j8B00",
                "4TiUsbbveB7ruRVxPGae",
                "acLeak1Zooy6RIvyOOXc"
            ),
            userOmrs = emptyList()
        )
    )

    val quizState = _quizState.asStateFlow()
}
