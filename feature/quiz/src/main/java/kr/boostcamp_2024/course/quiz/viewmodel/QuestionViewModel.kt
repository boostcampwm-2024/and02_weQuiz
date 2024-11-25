package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.boostcamp_2024.course.quiz.navigation.QuestionRoute
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizId: String = savedStateHandle.toRoute<QuestionRoute>().quizId

    init {
        Log.d("QuestionViewModel", "quizId: $quizId")
    }
}
